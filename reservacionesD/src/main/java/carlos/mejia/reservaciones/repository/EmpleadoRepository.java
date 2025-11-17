package carlos.mejia.reservaciones.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import carlos.mejia.reservaciones.entity.Empleado;

public interface EmpleadoRepository extends JpaRepository<Empleado, Integer> {

	//Recuperar por su id del puesto
	List<Empleado> findByPuesto_IdPuesto(Integer idPuesto);
	
	//Método combinado: busca por Nombre (LIKE) Y Puesto (ID)
    List<Empleado> findByNombreContainingIgnoreCaseAndPuesto_IdPuesto(String nombre, Integer idPuesto);
    
    //Método solo por Nombre (LIKE), si no se proporciona el puesto.
    List<Empleado> findByNombreContainingIgnoreCase(String nombre);	
    
    
    
    Optional<Empleado> findByIdUsuario(Integer idUsuario); 
}
