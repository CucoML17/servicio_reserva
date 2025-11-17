package carlos.mejia.reservaciones.entity;

import java.util.Set;

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
@Table(name="mesa")
public class Mesa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idMesa;

    @Column(name="numero", nullable = false)
    private Integer numero;

    @Column(name="capacidad", nullable = false)
    private Integer capacidad;

    @Column(name="ubicacion", nullable = false)
    private String ubicacion;
    
    @Column(name="estatus", nullable = false)
    private Integer estatus;    

    //Su relación con reservas
    @OneToMany(mappedBy = "mesa")
    private Set<Reservar> reservas;
}
