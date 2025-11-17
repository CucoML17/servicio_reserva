package carlos.mejia.reservaciones.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder // Añadimos Builder para facilitar la creación de instancias
public class ReservaCompletaDto {

    private Integer idReserva;
    
    // Información del Cliente
    private String nombreCliente;
    
    private Integer idCliente; //
    
    // Información de la Mesa
    private Integer noMesa; // El número de mesa, no el ID
    
    // Información de la Reserva
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "America/Mexico_City") 
    private Date fecha;
    
    @JsonFormat(pattern = "HH:mm:ss", timezone = "America/Mexico_City") 
    private Date hora;
    
    private Integer estatus;
}