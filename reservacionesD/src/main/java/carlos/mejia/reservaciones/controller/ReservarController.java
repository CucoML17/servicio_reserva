package carlos.mejia.reservaciones.controller;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
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

import carlos.mejia.reservaciones.dto.ReservaCompletaDto;
import carlos.mejia.reservaciones.dto.ReservaVistaClienteDto;
import carlos.mejia.reservaciones.dto.ReservarDto;
import carlos.mejia.reservaciones.service.ReservarService;
import lombok.RequiredArgsConstructor;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/reservaciones")
@RequiredArgsConstructor
public class ReservarController {
    
    private final ReservarService reservarService;
    
    //Guardar reserva
    @PostMapping("/guardar")
    public ResponseEntity<ReservarDto> save(@RequestBody ReservarDto dto) {
        ReservarDto savedDto = reservarService.save(dto);
        return new ResponseEntity<>(savedDto, HttpStatus.CREATED);
    }
    
    //Lista de reservas
    @GetMapping("/listat")
    public ResponseEntity<List<ReservarDto>> findAll() {
        return ResponseEntity.ok(reservarService.findAll());
    }
    
    //Encontrar por id
    @GetMapping("/buscaid/{id}")
    public ResponseEntity<ReservarDto> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(reservarService.findById(id));
    }

    //Actualizar
    @PutMapping("/actualizar/{id}") 
    public ResponseEntity<ReservarDto> update(@PathVariable Integer id, @RequestBody ReservarDto dto) {
        ReservarDto updatedDto = reservarService.update(id, dto);
        return ResponseEntity.ok(updatedDto);
    }

    //Borrar por id
    @DeleteMapping("/eliminar/{id}") 
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        reservarService.delete(id);
        return ResponseEntity.ok("Reserva eliminada");
    }
    
    
    //Reservas bien y filtradas
    @GetMapping("/completa")
    public ResponseEntity<List<ReservaCompletaDto>> getReservasCompletasByFecha(
            @RequestParam(name = "fecha", required = false) 
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date fecha) {
        
        // Llamamos al nuevo método del servicio. 
        List<ReservaCompletaDto> reservas = reservarService.getReservasCompletasByFecha(fecha);
        
        return ResponseEntity.ok(reservas);
    }    
    
    
    //Obtener todas las reservas de un cliente, con filtro opcional de fecha
    @GetMapping("/cliente/{idCliente}")
    public ResponseEntity<List<ReservaVistaClienteDto>> getReservasByCliente(
            @PathVariable Integer idCliente,
            @RequestParam(name = "fecha", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date fecha) {
        
        List<ReservaVistaClienteDto> reservas = reservarService.getReservasByClienteIdAndFecha(idCliente, fecha);
        
        if (reservas.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content
        }
        
        return ResponseEntity.ok(reservas);
    }    
}