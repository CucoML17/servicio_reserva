package carlos.mejia.reservaciones.service;

import java.util.List;

import carlos.mejia.reservaciones.dto.MesaDto;



public interface MesaService {
	//Guardar Mesa
    MesaDto save(MesaDto request);
    
    //Lista de todas las mesas
    List<MesaDto> findAll();
    
    //Por id
    MesaDto findById(Integer id);
    
    //Editar
    MesaDto update(Integer id, MesaDto  request);
    
    //Borrar
    void delete(Integer id);
    
    
}
