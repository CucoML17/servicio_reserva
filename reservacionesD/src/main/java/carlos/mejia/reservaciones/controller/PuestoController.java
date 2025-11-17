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

import carlos.mejia.reservaciones.dto.PuestoDto;

import carlos.mejia.reservaciones.service.PuestoService;
import lombok.RequiredArgsConstructor;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/puestos")
@RequiredArgsConstructor
public class PuestoController {
    
    private final PuestoService puestoService;
    
    //Crear puesto
    @PostMapping("/guardar")
    public ResponseEntity<PuestoDto> create(@RequestBody PuestoDto dto) {
    	PuestoDto savedDto = puestoService.save(dto);
        return new ResponseEntity<>(savedDto, HttpStatus.CREATED);
    }
    
    //Listado de puestos
    @GetMapping("/listat")
    public ResponseEntity<List<PuestoDto>> findAll() {
        return ResponseEntity.ok(puestoService.findAll());
    }
    
    //Encontrar por ID
    @GetMapping("/buscaid/{id}")
    public ResponseEntity<PuestoDto> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(puestoService.findById(id));
    }
    
    //Actualizar
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<PuestoDto> update(@PathVariable Integer id, @RequestBody PuestoDto dto) {
    	PuestoDto updatedDto = puestoService.update(id, dto);
        return ResponseEntity.ok(updatedDto);
    }
    
    //Borrar puesto
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        puestoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}