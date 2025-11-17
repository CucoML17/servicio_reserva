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

import carlos.mejia.reservaciones.dto.AtenderDto;
import carlos.mejia.reservaciones.dto.EmpleadoDto;
import carlos.mejia.reservaciones.service.AtenderService;
import lombok.RequiredArgsConstructor;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/atender")
@RequiredArgsConstructor
public class AtenderController {
    
    private final AtenderService atenderService;
    
    //Insertar
    @PostMapping("/guardar")
    public ResponseEntity<AtenderDto> save(@RequestBody AtenderDto dto) {
      
        AtenderDto savedDto = atenderService.save(dto);
        return new ResponseEntity<>(savedDto, HttpStatus.CREATED);
    }
    
   //El listado de todos
    @GetMapping("/listat")
    public ResponseEntity<List<AtenderDto>> findAll() {
        return ResponseEntity.ok(atenderService.findAll());
    }
    
  
    //Encontrar por id (compuesta)
    @GetMapping("/buscaid/{idEmpleado}/{idVenta}")
    public ResponseEntity<AtenderDto> findById(
        @PathVariable Integer idEmpleado, 
        @PathVariable Integer idVenta
    ) {
   
        AtenderDto dto = atenderService.findById(idEmpleado, idVenta);
        return ResponseEntity.ok(dto);
    }
    
   //Actualizar
    @PutMapping("/actualizar/{idEmpleado}/{idVenta}")
    public ResponseEntity<AtenderDto> update(
        @PathVariable Integer idEmpleado, 
        @PathVariable Integer idVenta, 
        @RequestBody AtenderDto dto
    ) {
    
        AtenderDto updatedDto = atenderService.update(idEmpleado, idVenta, dto);
        return ResponseEntity.ok(updatedDto);
    }
    
    
    //Borrar
    @DeleteMapping("/eliminar/{idEmpleado}/{idVenta}")
    public ResponseEntity<Void> delete(
        @PathVariable Integer idEmpleado, 
        @PathVariable Integer idVenta
    ) {
       
        atenderService.delete(idEmpleado, idVenta);
        return ResponseEntity.noContent().build();
    }
    
    //Obtener Empleado a partir de la venta
    @GetMapping("/empleado/venta/{idVenta}")
    public ResponseEntity<EmpleadoDto> getEmpleadoByVentaId(@PathVariable Integer idVenta) {
        EmpleadoDto empleado = atenderService.getEmpleadoByVentaId(idVenta);
        return ResponseEntity.ok(empleado);
    }    
    
    
    @GetMapping("/reservaEmpleado/{idReserva}")
    public ResponseEntity<AtenderDto> getAtenderByReservaId(@PathVariable Integer idReserva) {
        try {
            AtenderDto atenderDto = atenderService.getAtenderByReservaId(idReserva);
            return ResponseEntity.ok(atenderDto);
        } catch (RuntimeException e) {
            // Manejamos la excepción como NotFound si no se encuentra la relación
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); 
        }
    }    
    
    
    
    @GetMapping("/empleado/{idEmpleado}")
    public ResponseEntity<List<AtenderDto>> getAtenderByEmpleadoId(@PathVariable Integer idEmpleado) {
        
        List<AtenderDto> atenderList = atenderService.findAtenderByEmpleadoId(idEmpleado);
        
        if (atenderList.isEmpty()) {
             return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        
        return ResponseEntity.ok(atenderList);
    }    
}