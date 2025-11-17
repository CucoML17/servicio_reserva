package carlos.mejia.reservaciones.serviceimpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import carlos.mejia.reservaciones.dto.PuestoDto;

import carlos.mejia.reservaciones.entity.Puesto;
import carlos.mejia.reservaciones.mapper.PuestoMapper;
import carlos.mejia.reservaciones.repository.PuestoRepository;
import carlos.mejia.reservaciones.service.PuestoService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PuestoServiceImpl implements PuestoService {
    
    private final PuestoRepository puestoRepository;
    
    //Guardar un puesto
    @Override
    public PuestoDto save(PuestoDto dto) {
        //Usa el mapper para convertir DTO a Entidad
        Puesto puesto = PuestoMapper.mapToPuesto(dto); 
        
        Puesto savedPuesto = puestoRepository.save(puesto);
        return PuestoMapper.mapToPuestoDto(savedPuesto);
    }
    
    //Lista de todos los puestos
    public List<PuestoDto> findAll() {
        
        // 1. Recuperamos la lista de los puestos del repositorio.
    	List<Puesto> puestos = puestoRepository.findAllByOrderByIdPuestoAsc();;
        
        // 2. Mapeo
        return puestos.stream()
                .map((puesto) -> PuestoMapper.mapToPuestoDto(puesto))
                .collect(Collectors.toList());
    }
    
    //Obtener un puesto por id
    @Override
    public PuestoDto findById(Integer id) {
        Puesto puesto = puestoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Puesto no encontrado con ID: " + id)); 
                
        return PuestoMapper.mapToPuestoDto(puesto);
    }
    
    //Actualizar el puesto
    @Override
    public PuestoDto update(Integer id, PuestoDto dto) {
        Puesto puesto = puestoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Puesto no encontrado con ID: " + id));
        
        //Actualizar
        puesto.setNombrePuesto(dto.getNombrePuesto());
        
        Puesto updatedPuesto = puestoRepository.save(puesto);
        return PuestoMapper.mapToPuestoDto(updatedPuesto);
    }
    
    //Borrar un puesto
    @Override
    public void delete(Integer id) {
        puestoRepository.deleteById(id);
    }
}