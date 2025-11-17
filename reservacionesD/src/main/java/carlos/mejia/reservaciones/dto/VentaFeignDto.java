package carlos.mejia.reservaciones.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor
public class VentaFeignDto {
    private Integer idventa;
    private double totalventa;

    private String fechaventa; //Es string para evitar conversiones de fechas constantes, total es para mostrar
    private Integer id_cliente;
}
