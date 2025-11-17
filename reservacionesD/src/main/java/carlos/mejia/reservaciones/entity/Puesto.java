package carlos.mejia.reservaciones.entity;

import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="puesto")
public class Puesto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idPuesto;

    @Column(nullable = false, unique = true)
    private String nombrePuesto;

    //Su relación uno a muchos con empleado
    //donde un puesto puede tener muchos empleados, pero un empleado solo un puesto.
    @OneToMany(mappedBy = "puesto")
    private Set<Empleado> empleados;
}
