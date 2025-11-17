package carlos.mejia.reservaciones.mapper;

import carlos.mejia.reservaciones.dto.AtenderDto;
import carlos.mejia.reservaciones.entity.Atender;

public class AtenderMapper {
    
    //1. Mapea de Entidad Atender a DTO de Salida
    public static AtenderDto mapToAtenderDto(Atender atender) {
        
        return new AtenderDto(
            //Extrae el ID de la entidad Empleado anidada
            atender.getEmpleado() != null ? atender.getEmpleado().getIdEmpleado() : null, 
            
            //Extrae el ID de Venta del AtenderId
            atender.getId() != null ? atender.getId().getIdVenta() : null 
        );
    }

    //2. Mapea de DTO de Entrada a Entidad Atender
    public static Atender mapToAtender(AtenderDto dto) {
        
 
        return new Atender(
            null, //El AtenderId será construido/asignado por el servicio
            null  //El Empleado será buscado/asignado por el servicio
        );
        
    }
}
