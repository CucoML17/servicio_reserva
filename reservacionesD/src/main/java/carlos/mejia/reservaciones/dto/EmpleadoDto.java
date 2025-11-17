package carlos.mejia.reservaciones.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor
public class EmpleadoDto {
    private Integer idEmpleado;
    private String nombre;
    private Integer idPuesto; 
    private Integer idUsuario;
    
}