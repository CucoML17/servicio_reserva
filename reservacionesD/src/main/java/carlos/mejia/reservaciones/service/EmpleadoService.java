package carlos.mejia.reservaciones.service;

import java.util.List;

import carlos.mejia.reservaciones.dto.EmpleadoDto;
import carlos.mejia.reservaciones.dto.EmpleadoLocalUpdateRequest;
import carlos.mejia.reservaciones.dto.EmpleadoRegistroRequest;
import carlos.mejia.reservaciones.dto.EmpleadoUpdateRequest;

public interface EmpleadoService {
   
	//Guardar
    EmpleadoDto save(EmpleadoDto dto); 
    
    //Listado total
    List<EmpleadoDto> findAll();
    
    //Por id
    EmpleadoDto findById(Integer id);
    
    //Actualizar
    EmpleadoDto update(Integer id, EmpleadoDto dto); 
    
    //Borrar
    void delete(Integer id);  
    
    //Recupear el listado por puesto del empleado
    List<EmpleadoDto> findByPuestoId(Integer idPuesto);    
    
    //Filtros estandarizados
    List<EmpleadoDto> filtrarEmpleados(String nombreBuscar, Integer idPuestoBuscar);
    
    
    //Registro
    EmpleadoDto registerEmpleadoWithOptionalUser(EmpleadoRegistroRequest request);
    
    
    //Por su usuario
    EmpleadoDto getEmpleadoByUserId(Integer idUsuario);
    
    //Editar:
    EmpleadoDto updateEmpleadoWithOptionalUser(Integer idEmpleado, EmpleadoUpdateRequest request);
    
    
    EmpleadoDto updateEmpleadoLocalData(Integer idEmpleado, EmpleadoLocalUpdateRequest request);
}