package carlos.mejia.reservaciones.mapper;

import carlos.mejia.reservaciones.dto.EmpleadoDto;
import carlos.mejia.reservaciones.entity.Empleado;

public class EmpleadoMapper {
    
    // 1. Mapea de Entidad Empleado a DTO de Salida
    public static EmpleadoDto mapToEmpleadoDto(Empleado empleado) {
        
        Integer idPuesto = null;
        //Si la relación existe en la entidad, extrae solo el ID
        if (empleado.getPuesto() != null) {
            idPuesto = empleado.getPuesto().getIdPuesto(); 
        }
        
        return new EmpleadoDto(
            empleado.getIdEmpleado(),
            empleado.getNombre(),
            idPuesto,
            empleado.getIdUsuario()
        );
    }

    //2. Mapea de DTO de Entrada a Entidad Empleado

    public static Empleado mapToEmpleado(EmpleadoDto empleadoDto) {
        return new Empleado(
            empleadoDto.getIdEmpleado(),
            empleadoDto.getNombre(),
            empleadoDto.getIdUsuario(),
            null, 
            null 
        );
    }
}