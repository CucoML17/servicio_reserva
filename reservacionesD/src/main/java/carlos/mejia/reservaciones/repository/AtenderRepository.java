package carlos.mejia.reservaciones.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import carlos.mejia.reservaciones.entity.Atender;
import carlos.mejia.reservaciones.entity.AtenderId;

public interface AtenderRepository extends JpaRepository<Atender, AtenderId> {
	Optional<Atender> findById_IdVenta(Integer idVenta);
	
	List<Atender> findById_IdEmpleado(Integer idEmpleado);
}
