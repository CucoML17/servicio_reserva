package carlos.mejia.reservaciones.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmpleadoLocalUpdateRequest {
    
    // Campos del Empleado
    private String nombre;
    private Integer idPuesto;
    // ID de Usuario a asignar (Puede ser null si no se asigna un usuario)
    private Integer idUsuario; 
    
}