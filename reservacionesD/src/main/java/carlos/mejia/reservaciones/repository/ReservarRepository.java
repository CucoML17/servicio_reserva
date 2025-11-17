package carlos.mejia.reservaciones.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import carlos.mejia.reservaciones.entity.Reservar;

public interface ReservarRepository extends JpaRepository<Reservar, Integer>{

	//Busca todas las reservas que coincidan exactamente con la fecha
    List<Reservar> findByFecha(Date fecha);
    
    List<Reservar> findAll();	
    
    
    // 1. Encontrar por ID de Cliente
    List<Reservar> findByIdCliente(Integer idCliente);
    
    // 2. Encontrar por ID de Cliente y Fecha específica
    List<Reservar> findByIdClienteAndFecha(Integer idCliente, Date fecha);    
}
