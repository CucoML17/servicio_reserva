package carlos.mejia.reservaciones.dto;

import lombok.Data;

@Data
public class EmpleadoUpdateRequest {
    
    // 1. Campos del Empleado
    private String nombre;
    private Integer idPuesto;
    
    // 2. Campos del Usuario (Para actualización en authservice, opcionales)
    // El idUsuario se obtiene del Empleado existente.
    private String username;
    private Integer idPerfil; // El nuevo Perfil/Rol
}
