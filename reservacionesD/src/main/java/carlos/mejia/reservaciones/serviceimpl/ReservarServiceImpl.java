package carlos.mejia.reservaciones.serviceimpl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import carlos.mejia.reservaciones.dto.ClienteFeignDto;
import carlos.mejia.reservaciones.dto.ReservaCompletaDto;
import carlos.mejia.reservaciones.dto.ReservaVistaClienteDto;
import carlos.mejia.reservaciones.dto.ReservarDto;
import carlos.mejia.reservaciones.entity.Mesa;
import carlos.mejia.reservaciones.entity.Reservar;
import carlos.mejia.reservaciones.feign.ClienteFeignClient;
import carlos.mejia.reservaciones.mapper.ReservarMapper;
import carlos.mejia.reservaciones.repository.MesaRepository;
import carlos.mejia.reservaciones.repository.ReservarRepository;
import carlos.mejia.reservaciones.service.ReservarService;
import feign.FeignException.NotFound;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservarServiceImpl implements ReservarService {
    
    private final ReservarRepository reservarRepository;
    private final MesaRepository mesaRepository;
    private final ClienteFeignClient clienteFeignClient; 
    //private final VentaFeignClient ventaFeignClient;     
    

    //Método privado para manejar la validación de dependencias
    private void validateDependencies(ReservarDto dto) { 
        //1. Validarque exista la Mesa
        mesaRepository.findById(dto.getIdMesa()) 
        .orElseThrow(() -> new RuntimeException("Mesa no encontrada con ID: " + dto.getIdMesa()));
        
        //2. Validar que exista el cliente en el otro microservicio
        try {
            clienteFeignClient.getClienteById(dto.getIdCliente());
        } catch (NotFound ex) {
            throw new RuntimeException("Cliente no encontrado en microservicio Cliente con ID: " + dto.getIdCliente());
        }
        
        //YA NOSE HACE CAMBIAMOS 3. Validar Venta (externo - Feign, solo si se proporciona el ID)
        /*
        if (dto.getIdVenta() != null) {
            try {
                ventaFeignClient.getVentaById(dto.getIdVenta());
            } catch (NotFound ex) {
                throw new RuntimeException("Venta no encontrada con ID: " + dto.getIdVenta());
            }
        }*/
    }

    //Ahora sí, el CRUD

    //Guardar una reserva
    @Override
    public ReservarDto save(ReservarDto dto) {
        validateDependencies(dto);
        
        //La mesa
        Mesa mesa = mesaRepository.findById(dto.getIdMesa()).get(); 
        
        Reservar reserva = new Reservar();
        reserva.setFecha(dto.getFecha());
        reserva.setHora(dto.getHora());
        reserva.setMesa(mesa);
        reserva.setIdCliente(dto.getIdCliente());
       // reserva.setIdVenta(dto.getIdVenta());
        
        reserva.setEstatus(dto.getEstatus() != null ? dto.getEstatus() : 0);
        
        Reservar savedReserva = reservarRepository.save(reserva);
        return ReservarMapper.mapToReservarDto(savedReserva);
    }
    
    //Obtener el listado de reservas
    @Override
    public List<ReservarDto> findAll() {
        
        return reservarRepository.findAll().stream()
                .map(ReservarMapper::mapToReservarDto)
                .collect(Collectors.toList());
    }
    
    //Encontrar una reserva por su id
    @Override
    public ReservarDto findById(Integer id) {
        
        Reservar reserva = reservarRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada con ID: " + id));
        return ReservarMapper.mapToReservarDto(reserva);
    }
    
    //Actualizar una reserva
    @Override
    public ReservarDto update(Integer id, ReservarDto dto) {
        Reservar reservaExistente = reservarRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva a actualizar no encontrada con ID: " + id));

        validateDependencies(dto);

        //La mesa
        Mesa nuevaMesa = mesaRepository.findById(dto.getIdMesa()).get();
        
        //Actualiza los atributos
        reservaExistente.setFecha(dto.getFecha());
        reservaExistente.setHora(dto.getHora());
        
        reservaExistente.setEstatus(dto.getEstatus());
        
        reservaExistente.setMesa(nuevaMesa); 
        reservaExistente.setIdCliente(dto.getIdCliente()); 
        //reservaExistente.setIdVenta(dto.getIdVenta()); 
        
        Reservar updatedReserva = reservarRepository.save(reservaExistente);
        return ReservarMapper.mapToReservarDto(updatedReserva);
    }
    
    //Borrar una reserva directamente
    @Override
    public void delete(Integer id) {
        
        reservarRepository.deleteById(id);
        
    }
    
    
    //Para las reservas:
    
    @Override
    public List<ReservaCompletaDto> getReservasCompletasByFecha(Date fecha) {
        
        List<Reservar> reservas;
        
        //1. Obtener datos base (con o sin filtro de fecha)
        if (fecha != null) {
            reservas = reservarRepository.findByFecha(fecha);
        } else {
            reservas = reservarRepository.findAll();
        }
        
        //2. Mapear y enriquecer cada reserva
        return reservas.stream()
            .map(reserva -> {
                String nombreCliente = "Cliente Desconocido";
                
                //A. Obtener el nombre del Cliente (Llamada Feign)
                try {
                    ClienteFeignDto cliente = clienteFeignClient.getClienteById(reserva.getIdCliente());
                    if (cliente != null) {
                        nombreCliente = cliente.getNombrecliente();
                    }
                } catch (Exception e) {
                    System.err.println("Error Feign al obtener Cliente ID " + reserva.getIdCliente() + ": " + e.getMessage());
                    //Dejamos el nombre por defecto si falla la conexión o el ID no existe
                }
                
                //B. Construir el DTO Completo
                return ReservaCompletaDto.builder()
                        .idReserva(reserva.getIdReserva())
                        .idCliente(reserva.getIdCliente())
                        .nombreCliente(nombreCliente)
                        .noMesa(reserva.getMesa().getNumero()) // Obtener el número de la Mesa
                        .fecha(reserva.getFecha())
                        .hora(reserva.getHora())
                        .estatus(reserva.getEstatus())
                        .build();
            })
            .collect(Collectors.toList());
    }
    
    
    
    @Override
    public List<ReservaVistaClienteDto> getReservasByClienteIdAndFecha(Integer idCliente, Date fecha) {
        
        //1. Validar que el cliente exista antes de buscar reservas
        try {
            clienteFeignClient.getClienteById(idCliente);
        } catch (NotFound ex) {
            throw new RuntimeException("Cliente no encontrado en microservicio Cliente con ID: " + idCliente);
        }

        //2. Obtener las reservas de la base de datos
        List<Reservar> reservas;
        
        if (fecha != null) {
            //Buscar por ID de Cliente Y Fecha
            reservas = reservarRepository.findByIdClienteAndFecha(idCliente, fecha);
        } else {
            //Buscar solo por ID de Cliente
            reservas = reservarRepository.findByIdCliente(idCliente);
        }
        
        // 3.Mapear al DTO simplificado (ReservaVistaClienteDto)
        return reservas.stream()
            .map(reserva -> ReservaVistaClienteDto.builder()
                .idReserva(reserva.getIdReserva())
                .noMesa(reserva.getMesa().getNumero())
                .ubicacionMesa(reserva.getMesa().getUbicacion())
                .fecha(reserva.getFecha())
                .hora(reserva.getHora())
                .estatus(reserva.getEstatus())
                .build())
            .collect(Collectors.toList());
    }    
    
}