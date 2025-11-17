package carlos.mejia.reservaciones.serviceimpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import carlos.mejia.reservaciones.dto.AtenderDto;
import carlos.mejia.reservaciones.dto.EmpleadoDto;
import carlos.mejia.reservaciones.dto.VentaListDto;
import carlos.mejia.reservaciones.entity.Atender;
import carlos.mejia.reservaciones.entity.AtenderId;
import carlos.mejia.reservaciones.entity.Empleado;
import carlos.mejia.reservaciones.feign.VentaFeignClient;
import carlos.mejia.reservaciones.mapper.AtenderMapper;
import carlos.mejia.reservaciones.mapper.EmpleadoMapper;
import carlos.mejia.reservaciones.repository.AtenderRepository;
import carlos.mejia.reservaciones.repository.EmpleadoRepository;
import carlos.mejia.reservaciones.service.AtenderService;
import feign.FeignException;
import feign.FeignException.NotFound;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AtenderServiceImpl implements AtenderService {
    
    private final AtenderRepository atenderRepository;
    private final EmpleadoRepository empleadoRepository;
    private final VentaFeignClient ventaFeignClient;
    
    //Método auxiliar para crear la clave compuesta
    private AtenderId createAtenderId(AtenderDto dto) {
        return new AtenderId(dto.getIdEmpleado(), dto.getIdVenta());
    }
    
    //Método auxiliar para obtener la Entidad Empleado
    private Empleado getValidEmpleado(Integer idEmpleado) {
        return empleadoRepository.findById(idEmpleado)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado con ID: " + idEmpleado));
    }
    
    //Método auxiliar para validar la existencia de Venta en el otro microservicio
    private void validateVenta(Integer idVenta) {
        try {
           
            ventaFeignClient.getVentaById(idVenta); 
        } catch (NotFound ex) {
            throw new RuntimeException("Venta no encontrada en microservicio fondaSpringUnirF con ID: " + idVenta);
        } catch (FeignException ex) {
            throw new RuntimeException("Error de comunicación con el servicio de Ventas: " + ex.getMessage());
        }
    }

    @Override
    public AtenderDto save(AtenderDto dto) {
        
        //1. Validar existencia del Empleado y Venta
        Empleado empleado = getValidEmpleado(dto.getIdEmpleado());
        validateVenta(dto.getIdVenta());
        
        //2. Crear la clave primaria
        AtenderId id = createAtenderId(dto);
        
        if (atenderRepository.existsById(id)) {
             throw new RuntimeException("El registro Atender (Empleado: " + dto.getIdEmpleado() + ", Venta: " + dto.getIdVenta() + ") ya existe.");
        }
        
        //3. Mapear a Entidad (el Mapper solo crea la estructura base)
        Atender atender = AtenderMapper.mapToAtender(dto); 
        
        //4. Asignar las relaciones buscadas
        atender.setId(id);
        atender.setEmpleado(empleado); 
        
        Atender savedAtender = atenderRepository.save(atender);
        
        //5. Mapear a DTO de salida
        return AtenderMapper.mapToAtenderDto(savedAtender);
    }
    
    //Lista de todos los Atender
    @Override
    public List<AtenderDto> findAll() {

        return atenderRepository.findAll().stream()
                .map(AtenderMapper::mapToAtenderDto) 
                .collect(Collectors.toList());
    }
    
    //Encontrar por id
    @Override
    public AtenderDto findById(Integer idEmpleado, Integer idVenta) {
        AtenderId id = new AtenderId(idEmpleado, idVenta);
        Atender atender = atenderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Registro Atender no encontrado con Empleado ID: " + idEmpleado + ", Venta ID: " + idVenta));
                
        return AtenderMapper.mapToAtenderDto(atender);
    }
    
    
    //Actualizar un atender
    @Override
    public AtenderDto update(Integer idEmpleado, Integer idVenta, AtenderDto dto) {
        //1. Crear la clave del registro a buscar
        AtenderId existingId = new AtenderId(idEmpleado, idVenta);
        
        //2. Validar que el registro existe
        Atender atenderExistente = atenderRepository.findById(existingId)
                .orElseThrow(() -> new RuntimeException("Registro Atender no encontrado con Empleado ID: " + idEmpleado + ", Venta ID: " + idVenta));

        return AtenderMapper.mapToAtenderDto(atenderExistente); 
    }
    
    //Eliminar
    @Override
    public void delete(Integer idEmpleado, Integer idVenta) {
        AtenderId id = new AtenderId(idEmpleado, idVenta);
        
        if (!atenderRepository.existsById(id)) {
            throw new RuntimeException("Registro Atender no encontrado para eliminar con Empleado ID: " + idEmpleado + ", Venta ID: " + idVenta);
        }
        
        atenderRepository.deleteById(id);
    }
    
    

    @Override
    public EmpleadoDto getEmpleadoByVentaId(Integer idVenta) {
        
        // Implementación usando un método de repositorio, que debes crear:
        Atender atender = atenderRepository.findById_IdVenta(idVenta) // <-- ¡Necesitas agregar este método en AtenderRepository!
                .orElseThrow(() -> new RuntimeException("No hay empleado asignado a la Venta ID: " + idVenta));
                
        // Si el registro Atender existe, ya tiene la entidad Empleado cargada (ManyToOne)
        Empleado empleado = atender.getEmpleado();
        
        if (empleado == null) {
            throw new RuntimeException("El registro Atender para Venta ID: " + idVenta + " tiene un empleado nulo.");
        }
        
        return EmpleadoMapper.mapToEmpleadoDto(empleado);
    }
    
    
 // NUEVA IMPLEMENTACIÓN
    @Override
    public AtenderDto getAtenderByReservaId(Integer idReserva) {
        
        // 1. Obtener el ID de Venta a partir del ID de Reserva (Llamada Feign)
        VentaListDto ventaDto;
        try {
            ventaDto = ventaFeignClient.getVentaByReservaId(idReserva);
        } catch (NotFound ex) {
            // Este error puede significar que la Venta no existe o no está enlazada a la Reserva
            throw new RuntimeException("Venta no encontrada para la Reserva ID: " + idReserva);
        } catch (FeignException ex) {
            throw new RuntimeException("Error de comunicación con el servicio de Ventas: " + ex.getMessage());
        }

        if (ventaDto == null || ventaDto.getIdventa() == null) {
             throw new RuntimeException("El servicio de Ventas no devolvió una Venta válida para la Reserva ID: " + idReserva);
        }
        
        Integer idVenta = ventaDto.getIdventa();
        
        // 2. Buscar el registro Atender por el ID de Venta
        // Reutilizamos la lógica del método existente (findById_IdVenta)
        Atender atender = atenderRepository.findById_IdVenta(idVenta) 
                .orElseThrow(() -> new RuntimeException("No hay registro Atender asociado a la Venta ID: " + idVenta));
                
        // 3. Mapear y retornar
        return AtenderMapper.mapToAtenderDto(atender);
    }  
    
    
    //Obtiene todos los Atender de un Empleado
    @Override
    public List<AtenderDto> findAtenderByEmpleadoId(Integer idEmpleado) {
        
        // 1. Buscar todos los registros Atender asociados a ese Empleado
        List<Atender> atenderList = atenderRepository.findById_IdEmpleado(idEmpleado);
        
        // 2. Mapear la lista de entidades a DTOs
        return atenderList.stream()
                .map(AtenderMapper::mapToAtenderDto)
                .collect(Collectors.toList());
    }    
    
}