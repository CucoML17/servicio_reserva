package carlos.mejia.reservaciones.serviceimpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import carlos.mejia.reservaciones.dto.MesaDto;
import carlos.mejia.reservaciones.entity.Mesa;
import carlos.mejia.reservaciones.mapper.MesaMapper;
import carlos.mejia.reservaciones.repository.MesaRepository;
import carlos.mejia.reservaciones.service.MesaService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MesaServiceImpl implements MesaService {
    
    private final MesaRepository mesaRepository;
    
    //Guardar una mesa
    @Override
    public MesaDto save(MesaDto dto) {
        
        Mesa mesa = MesaMapper.mapToMesa(dto);
        
        Mesa savedMesa = mesaRepository.save(mesa);
        return MesaMapper.mapToMesaDto(savedMesa);
    }
    
    //Lista de todas las mesas
    @Override
    public List<MesaDto> findAll() {
        
        return mesaRepository.findAll().stream()
                .map(MesaMapper::mapToMesaDto)
                .collect(Collectors.toList());
    }
    
    //Buscar mesa por ID
    @Override
    public MesaDto findById(Integer id) {
        
        Mesa mesa = mesaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mesa no encontrada con ID: " + id));
        return MesaMapper.mapToMesaDto(mesa);
    }
    
    //Actualizar mesa
    @Override
    public MesaDto update(Integer id, MesaDto updateDto) {
        Mesa mesaExistente = mesaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mesa no encontrada con ID: " + id));

        mesaExistente.setNumero(updateDto.getNumero());
        mesaExistente.setCapacidad(updateDto.getCapacidad());
        mesaExistente.setUbicacion(updateDto.getUbicacion());
        
        mesaExistente.setEstatus(updateDto.getEstatus());
        
        Mesa updatedMesa = mesaRepository.save(mesaExistente);
        
        return MesaMapper.mapToMesaDto(updatedMesa);
    }
    
    //Eliminar mesa
    @Override
    public void delete(Integer id) {
       
        mesaRepository.deleteById(id);
    }
}