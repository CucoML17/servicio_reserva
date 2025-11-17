package carlos.mejia.reservaciones.service;

import java.util.Date;
import java.util.List;

import carlos.mejia.reservaciones.dto.ReservaCompletaDto;
import carlos.mejia.reservaciones.dto.ReservaVistaClienteDto;
import carlos.mejia.reservaciones.dto.ReservarDto;


public interface ReservarService {
	//Guardar reserva
    ReservarDto save(ReservarDto dto);
    
    //Listado de todas las reservas
    List<ReservarDto> findAll();
    
    //Encontrar por id
    ReservarDto findById(Integer id);
    
    //Actualizar
    ReservarDto update(Integer id, ReservarDto dto);
    
    //Borrar
    void delete(Integer id); 
    
    
    //Reserva por fecha
    List<ReservaCompletaDto> getReservasCompletasByFecha(Date fecha);
    
    
    List<ReservaVistaClienteDto> getReservasByClienteIdAndFecha(Integer idCliente, Date fecha);
    
}