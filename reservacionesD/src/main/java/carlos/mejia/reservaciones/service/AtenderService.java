package carlos.mejia.reservaciones.service;

import java.util.List;

import carlos.mejia.reservaciones.dto.AtenderDto;
import carlos.mejia.reservaciones.dto.EmpleadoDto;


public interface AtenderService {
    
    //Guardar
    AtenderDto save(AtenderDto dto); 
    
    //Listado total
    List<AtenderDto> findAll();
    
    //Por id
    AtenderDto findById(Integer idEmpleado, Integer idVenta);
    
    //Actualizar
    AtenderDto update(Integer idEmpleado, Integer idVenta, AtenderDto dto);
    
    //Borrar
    void delete(Integer idEmpleado, Integer idVenta);
    
    //Obtener el empleado que atiende
    EmpleadoDto getEmpleadoByVentaId(Integer idVenta);
    
    
    AtenderDto getAtenderByReservaId(Integer idReserva);
    
    
    
    List<AtenderDto> findAtenderByEmpleadoId(Integer idEmpleado);
    
    
}