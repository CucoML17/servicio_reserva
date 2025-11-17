package carlos.mejia.reservaciones.entity;

import jakarta.persistence.Embeddable;
import lombok.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class AtenderId implements Serializable {
    
    //idEmpleado de este microservicio
    private Integer idEmpleado; 

    //idVenta del microservicio fondaSpringUnirF (ID externo)
    private Integer idVenta; 
}