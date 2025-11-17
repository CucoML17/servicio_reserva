package carlos.mejia.reservaciones.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteSimpleDto {
    private Integer idcliente;
    private String nombre;
    private String telefono;
}
