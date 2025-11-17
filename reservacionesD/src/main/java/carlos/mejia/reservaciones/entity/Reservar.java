package carlos.mejia.reservaciones.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;
import java.sql.Time; // Usaremos java.sql.Time o java.time.LocalTime (si usas Java 8+)

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="reservar")
public class Reservar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idReserva; 

    @Temporal(TemporalType.DATE)
    @Column(name="fecha", nullable = false)
    private Date fecha;

    @Temporal(TemporalType.TIME)
    @Column(name="hora", nullable = false)
    private Date hora; //La fecha de reservacion
    
    
    @Column(name="estatus", nullable = false)
    private Integer estatus;    

    //ManyToOne con Mesa
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_mesa", nullable = false)
    private Mesa mesa;

    //Referencia al ID del microservicio Cliente (La externa ese idCliente es del embedable)
    @Column(name = "id_cliente", nullable = false)
    private Integer idCliente;

    //Se cambió----------
    //Referencia al ID del microservicio Venta (ID externo)
    //@Column(name = "id_venta", nullable = true)
    //private Integer idVenta;
}