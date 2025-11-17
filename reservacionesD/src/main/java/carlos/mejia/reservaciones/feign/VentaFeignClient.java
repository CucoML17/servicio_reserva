package carlos.mejia.reservaciones.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import carlos.mejia.reservaciones.dto.VentaFeignDto;
import carlos.mejia.reservaciones.dto.VentaListDto;

@FeignClient(name = "fondaSpringUnirF") 
public interface VentaFeignClient {
    
    @GetMapping("/api/ventas/buscaid/{id}")
    VentaFeignDto getVentaById(@PathVariable("id") Integer id);
    
    @GetMapping("/api/ventas/ventareserva/{idReserva}")
    VentaListDto getVentaByReservaId(@PathVariable("idReserva") Integer idReserva);
}
