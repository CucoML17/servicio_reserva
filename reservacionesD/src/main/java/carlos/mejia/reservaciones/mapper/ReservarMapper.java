package carlos.mejia.reservaciones.mapper;


import carlos.mejia.reservaciones.dto.ReservarDto;
import carlos.mejia.reservaciones.entity.Mesa;
import carlos.mejia.reservaciones.entity.Reservar;

public class ReservarMapper {
    
    //Entiad a DTO:
    public static ReservarDto mapToReservarDto(Reservar reservar) {
        return new ReservarDto(
            reservar.getIdReserva(),
            reservar.getFecha(),
            reservar.getHora(),
            reservar.getEstatus(),
            
            reservar.getMesa() != null ? reservar.getMesa().getIdMesa() : null,
            reservar.getIdCliente()
            //reservar.getIdVenta()
        );
    }

    //De DTO a entidad:
    public static Reservar mapToReservar(ReservarDto dto) {
        return new Reservar(
            dto.getIdReserva(),
            dto.getFecha(),
            dto.getHora(),
            dto.getEstatus(),
            null, 
            dto.getIdCliente()
            //dto.getIdVenta()
        );
    }

 
}
