package carlos.mejia.reservaciones.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import carlos.mejia.reservaciones.dto.ClienteFeignDto;

@FeignClient(name = "resurantespringf") 
public interface ClienteFeignClient {
    
    @GetMapping("/api/cliente/buscaid/{id}")
    ClienteFeignDto getClienteById(@PathVariable("id") Integer id);
}