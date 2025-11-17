package carlos.mejia.reservaciones.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import carlos.mejia.reservaciones.entity.Puesto;

public interface PuestoRepository extends JpaRepository<Puesto, Integer> {

	//Todos los puestos pero ordenados ascendentemente
	List<Puesto> findAllByOrderByIdPuestoAsc(); 
	
}
