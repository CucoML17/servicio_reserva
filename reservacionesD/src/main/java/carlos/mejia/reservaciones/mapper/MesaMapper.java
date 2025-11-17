package carlos.mejia.reservaciones.mapper;

import carlos.mejia.reservaciones.dto.MesaDto;

import carlos.mejia.reservaciones.entity.Mesa;

public class MesaMapper {
    
    //Mapea DTO completo (para consultas directas de Mesa)
    public static MesaDto mapToMesaDto(Mesa mesa) {
        return new MesaDto(
            mesa.getIdMesa(),
            mesa.getNumero(),
            mesa.getCapacidad(),
            mesa.getUbicacion(),
            mesa.getEstatus()
        );
    }
    
 
    
    //Mapear  DTO a Entidad
    public static Mesa mapToMesa(MesaDto mesaDto) {
        return new Mesa(
            mesaDto.getIdMesa(),
            mesaDto.getNumero(),
            mesaDto.getCapacidad(),
            mesaDto.getUbicacion(),
            mesaDto.getEstatus(),
            null //La lista de reservas se ignora, portque sino, crea recursividad infinita
        );
    }
}