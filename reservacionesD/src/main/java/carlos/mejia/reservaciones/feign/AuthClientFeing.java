package carlos.mejia.reservaciones.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import carlos.mejia.reservaciones.dto.RegistroRequestDto;
import carlos.mejia.reservaciones.dto.UsuarioResponseDto;
import carlos.mejia.reservaciones.dto.UsuarioUpdateRequestDto;

@FeignClient(name = "authservicio")
public interface AuthClientFeing {

	@PostMapping("/api/auth/registrar/empleados")
    UsuarioResponseDto registerStaff(@RequestBody RegistroRequestDto request);	
	
	@PutMapping("/api/auth/actualiza/sincontra/usuario/{id}")
    UsuarioResponseDto updateUsuarioSinContrasenia(
        @PathVariable("id") Integer idUsuario,
        @RequestBody UsuarioUpdateRequestDto request
    );
	
	@GetMapping("/api/auth/estatus/{username}")
    Integer getEstatusByUsername(
        @RequestHeader("Authorization") String token,
        @PathVariable("username") String username
    );	
}
