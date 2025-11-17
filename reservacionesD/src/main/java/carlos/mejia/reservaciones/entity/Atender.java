package carlos.mejia.reservaciones.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="atender")
public class Atender {

    @EmbeddedId
    private AtenderId id;

    //Relación ManyToOne con Empleado
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idEmpleado")
    @JoinColumn(name = "id_empleado")
    private Empleado empleado;

}
