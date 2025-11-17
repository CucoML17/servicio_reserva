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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import carlos.mejia.reservaciones.dto.EmpleadoDto;
import carlos.mejia.reservaciones.dto.EmpleadoLocalUpdateRequest;
import carlos.mejia.reservaciones.dto.EmpleadoRegistroRequest;
import carlos.mejia.reservaciones.dto.EmpleadoUpdateRequest;
import carlos.mejia.reservaciones.service.EmpleadoService;
import lombok.RequiredArgsConstructor;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/empleados")
@RequiredArgsConstructor
public class EmpleadoController {
    
    private final EmpleadoService empleadoService;
    
    //Guardar empleado
    @PostMapping("/guardar")
    public ResponseEntity<EmpleadoDto> create(@RequestBody EmpleadoDto dto) {
    	EmpleadoDto savedDto = empleadoService.save(dto);
        return new ResponseEntity<>(savedDto, HttpStatus.CREATED);
    }
    
    //Lista de todos los empleados
    @GetMapping("/listat")
    public ResponseEntity<List<EmpleadoDto>> findAll() {
        return ResponseEntity.ok(empleadoService.findAll());
    }
    
    //Obtener por id
    @GetMapping("/buscaid/{id}")
    public ResponseEntity<EmpleadoDto> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(empleadoService.findById(id));
    }
    
    //Actualizar
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<EmpleadoDto> update(@PathVariable Integer id, @RequestBody EmpleadoDto dto) {
    	EmpleadoDto updatedDto = empleadoService.update(id, dto);
        return ResponseEntity.ok(updatedDto);
    }
    
    //Borrar
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        empleadoService.delete(id);
        return ResponseEntity.noContent().build();
    }
    

    //Buscar por puesto del empleado.
 
    @GetMapping("/buscarEmpleado")
    public ResponseEntity<List<EmpleadoDto>> filterEmpleados(
            @RequestParam(name = "nombre", required = false) String nombre,
            @RequestParam(name = "idPuesto", required = false) Integer idPuesto) {
        
        List<EmpleadoDto> empleados = empleadoService.filtrarEmpleados(nombre, idPuesto);
        
        return ResponseEntity.ok(empleados);
    }
    
    
    
    
    
    
    @PostMapping("/registrocompleto")
    public ResponseEntity<EmpleadoDto> registerEmpleado(@RequestBody EmpleadoRegistroRequest request) {
        
        try {
            EmpleadoDto nuevoEmpleado = empleadoService.registerEmpleadoWithOptionalUser(request);
            return new ResponseEntity<>(nuevoEmpleado, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST); 
        }
    }    
    
    
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<EmpleadoDto> getEmpleadoByUserId(@PathVariable("idUsuario") Integer idUsuario) {
        
        EmpleadoDto empleadoEncontrado = empleadoService.getEmpleadoByUserId(idUsuario);
        
        return ResponseEntity.ok(empleadoEncontrado);
    }    
    
    @PutMapping("/actualiza/completo/{id}")
    public ResponseEntity<EmpleadoDto> updateEmpleadoCompleto(
            @PathVariable("id") Integer idEmpleado,
            @RequestBody EmpleadoUpdateRequest request) {
        
        try {
            EmpleadoDto empleadoActualizado = empleadoService.updateEmpleadoWithOptionalUser(idEmpleado, request);
            return new ResponseEntity<>(empleadoActualizado, HttpStatus.OK);
        } catch (RuntimeException e) {
            // Manejo de errores
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
    
    
    @PutMapping("/actualizarespecial/{id}")
    public ResponseEntity<EmpleadoDto> updateEmpleadoEspecial(
            @PathVariable("id") Integer idEmpleado,
            @RequestBody EmpleadoLocalUpdateRequest request) {
        
        try {
            EmpleadoDto empleadoActualizado = empleadoService.updateEmpleadoLocalData(idEmpleado, request);
            return new ResponseEntity<>(empleadoActualizado, HttpStatus.OK);
        } catch (RuntimeException e) {
            // Manejo de errores (Empleado no encontrado, Puesto no encontrado)
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }    
    
}