package carlos.mejia.reservaciones.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import carlos.mejia.reservaciones.dto.MesaDto;
import carlos.mejia.reservaciones.service.MesaService;
import lombok.RequiredArgsConstructor;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/mesas")
@RequiredArgsConstructor
public class MesaController {
    
    private final MesaService mesaService;
    
    //Guardar mesa
    @PostMapping("/guardar")
    public ResponseEntity<MesaDto> create(@RequestBody MesaDto dto) {
        MesaDto savedDto = mesaService.save(dto);
        return new ResponseEntity<>(savedDto, HttpStatus.CREATED);
    }
    
    //Obtener todo el listado de mesas
    @GetMapping("/listat")
    public ResponseEntity<List<MesaDto>> findAll() {
        return ResponseEntity.ok(mesaService.findAll());
    }
    
    //Encontrar por ID
    @GetMapping("/buscaid/{id}")
    public ResponseEntity<MesaDto> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(mesaService.findById(id));
    }
    
    //Actualizar
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<MesaDto> update(@PathVariable Integer id, @RequestBody MesaDto dto) {
        MesaDto updatedDto = mesaService.update(id, dto);
        return ResponseEntity.ok(updatedDto);
    }
    
    //Borrar mesa
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        mesaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}