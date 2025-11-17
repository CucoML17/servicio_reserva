package carlos.mejia.reservaciones.entity;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name="empleado")
public class Empleado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idEmpleado;

    @Column(name="nombre", nullable = false)
    private String nombre;
    
    
    // NUEVO: Clave foránea lógica al servicio de autenticación
    @Column(name="id_usuario", unique = true, nullable = true) // <-- ¡AÑADIDO!
    private Integer idUsuario;
    

    //ManyToOne con Puesto
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_puesto", nullable = false)
    private Puesto puesto;

    //La lista de "Anteder" que ha realizado, del mucho a muchos
    @OneToMany(mappedBy = "empleado")
    private Set<Atender> atenciones;
}