package carlos.mejia.reservaciones.mapper;

import carlos.mejia.reservaciones.dto.PuestoDto;

import carlos.mejia.reservaciones.entity.Puesto;

public class PuestoMapper {
    
    //1. Mapea de Entidad Puesto a DTO de Salida
    public static PuestoDto mapToPuestoDto(Puesto puesto) {
        return new PuestoDto(
            puesto.getIdPuesto(),
            puesto.getNombrePuesto()
        );
    }
    
    //2. Mapea de DTO de Entrada a Entidad Puesto (para save/update)
    public static Puesto mapToPuesto(PuestoDto puestoDto) {
        return new Puesto(
            puestoDto.getIdPuesto(),
            puestoDto.getNombrePuesto(),
            null
        );
    }
}