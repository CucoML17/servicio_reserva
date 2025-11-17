package carlos.mejia.reservaciones.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservaVistaClienteDto {
    
    private Integer idReserva;
    private Integer noMesa; // Número de la Mesa
    private String ubicacionMesa; // Ubicación de la Mesa (Opcional, pero útil)
    private Date fecha;
    private Date hora;
    private Integer estatus;
}