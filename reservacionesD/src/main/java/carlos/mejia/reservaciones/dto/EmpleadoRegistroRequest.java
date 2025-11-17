package carlos.mejia.reservaciones.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public class EmpleadoRegistroRequest {
    
    //Campos del Empleado
    private String nombre;
    private Integer idPuesto;
    
    //Campos del Usuario (solo para el registro, opcionales según el puesto)
    private String username;
    private String password;
    private Integer idPerfil;
}