package carlos.mejia.reservaciones.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import carlos.mejia.reservaciones.entity.Mesa;

public interface MesaRepository extends JpaRepository<Mesa, Integer> {

	//Recuperar por el número de mesa
	Mesa findByNumero(Integer numero); 
}
