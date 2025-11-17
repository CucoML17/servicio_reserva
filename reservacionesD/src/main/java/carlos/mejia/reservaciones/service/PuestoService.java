package carlos.mejia.reservaciones.service;

import java.util.List;

import carlos.mejia.reservaciones.dto.PuestoDto;


public interface PuestoService {
    //Guardar Puesto
    PuestoDto save(PuestoDto dto); 
    
    //Lista de todos los puestos
    List<PuestoDto> findAll();
    
    //Encontrar por id
    PuestoDto findById(Integer id);
    
    //Actualizar puesto
    PuestoDto update(Integer id, PuestoDto dto); 
    
    //Borrar
    void delete(Integer id);
}