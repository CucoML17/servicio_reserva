package carlos.mejia.reservaciones.security;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        //Permite cualquier origen (el equivalente a @CrossOrigin("*"))
        configuration.setAllowedOrigins(Arrays.asList("*")); 
        
        // Permite los métodos que usas (GET, POST, PUT, DELETE, y CRUCIAL: OPTIONS)
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        
        //permitir el header Authorization y Content-Type
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type")); 
        
        //Permite enviar cookies (aunque JWT no las usa directamente, es buena práctica)
        configuration.setAllowCredentials(false); 

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        //Aplica esta configuración a todas las rutas
        source.registerCorsConfiguration("/**", configuration); 
        return source;
    }
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
		    //Aplicar la configuración CORS
		    .cors(cors -> cors.configurationSource(corsConfigurationSource()))
		    
            // 1. Deshabilitar CSRF (necesario para APIs REST sin sesiones)
            .csrf(csrf -> csrf.disable())
            
            // 2. Configurar las reglas de autorización
            .authorizeHttpRequests(auth -> auth

            	//Mesas
            	.requestMatchers("/api/mesas/listat").permitAll()
            	.requestMatchers("/api/mesas/buscaid/**").permitAll()
            	
            	.requestMatchers("/api/mesas/guardar").hasAnyAuthority("Supervisor", "Administrador")
            	.requestMatchers("/api/mesas/actualizar/**").hasAnyAuthority("Supervisor", "Administrador")
            	.requestMatchers("/api/mesas/eliminar/**").hasAnyAuthority("Supervisor", "Administrador")
            	
            	
            	//Atender
            	.requestMatchers("/api/atender/guardar").hasAnyAuthority("Cajero", "Administrador")
            	.requestMatchers("/api/atender/listat").hasAnyAuthority("Cajero", "Supervisor","Administrador")
            	.requestMatchers("/api/atender/buscaid/**").hasAnyAuthority("Cajero", "Supervisor","Administrador")
            	.requestMatchers("/api/atender/actualizar/**").hasAnyAuthority("Cajero", "Supervisor","Administrador")
            	.requestMatchers("/api/atender/eliminar/**").hasAnyAuthority("Cajero", "Supervisor","Administrador")
            	.requestMatchers("/api/atender/empleado/venta/**").hasAnyAuthority("Cliente", "Mesero", "Cajero", "Supervisor","Administrador")
            	
            	//Empleado
            	.requestMatchers("/api/empleados/guardar").hasAnyAuthority("Supervisor", "Administrador")
            	.requestMatchers("/api/empleados/listat").hasAnyAuthority("Cajero", "Supervisor", "Administrador")
            	.requestMatchers("/api/empleados/buscaid/**").hasAnyAuthority("Cliente", "Mesero", "Cajero", "Supervisor", "Administrador")
            	.requestMatchers("/api/empleados/actualizar/**").hasAnyAuthority("Supervisor", "Administrador", "Mesero", "Cajero")
            	.requestMatchers("/api/empleados/eliminar/**").hasAnyAuthority("Supervisor", "Administrador")
            	.requestMatchers("/api/empleados/buscarEmpleado/**").hasAnyAuthority("Cajero", "Supervisor", "Administrador")
            	.requestMatchers("/api/empleados/usuario/**").hasAnyAuthority("Mesero", "Cajero", "Supervisor", "Administrador")
            	
            	.requestMatchers("/api/empleados/actualizarespecial/**").hasAnyAuthority("Supervisor", "Administrador")
            	
            	
            	//Puesto
            	.requestMatchers("/api/puestos/guardar").hasAnyAuthority("Supervisor", "Administrador")
            	.requestMatchers("/api/puestos/listat").hasAnyAuthority("Supervisor", "Administrador", "Cajero", "Mesero")
            	.requestMatchers("/api/puestos/buscaid/**").hasAnyAuthority("Cliente", "Mesero", "Cajero", "Supervisor", "Administrador")
            	.requestMatchers("/api/puestos/actualizar/**").hasAnyAuthority("Supervisor", "Administrador")
            	.requestMatchers("/api/puestos/eliminar/**").hasAnyAuthority("Supervisor", "Administrador")
            	
            	//Reservar
            	.requestMatchers("/api/reservaciones/guardar").hasAnyAuthority("Cliente", "Cajero", "Administrador")
            	.requestMatchers("/api/reservaciones/listat").hasAnyAuthority("Cliente", "Cajero", "Supervisor", "Administrador")
            	.requestMatchers("/api/reservaciones/buscaid/**").hasAnyAuthority("Cliente", "Mesero", "Cajero", "Supervisor", "Administrador")
            	.requestMatchers("/api/reservaciones/actualizar/**").hasAnyAuthority("Cliente", "Cajero", "Administrador")
            	.requestMatchers("/api/reservaciones/eliminar/**").hasAnyAuthority("Cliente", "Cajero", "Administrador")            	
            	.requestMatchers("/api/reservaciones/completa/**").hasAnyAuthority("Cajero", "Supervisor", "Administrador")
            	
            	.requestMatchers("/api/atender/reservaEmpleado/**").hasAnyAuthority("Cajero", "Supervisor", "Administrador")
            	
            	//Registro de empleados:
            	.requestMatchers("/api/empleados/registrocompleto").hasAnyAuthority("Supervisor", "Administrador")
            	
            	
            	.requestMatchers("/api/atender/empleado/**").hasAnyAuthority("Mesero", "Cajero", "Supervisor", "Administrador")
            	
                .anyRequest().authenticated()
            )
            
            //3. Establecer la política de sesión a STATELESS (CRUCIAL para JWT)
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            
            //4. Añadir nuestro filtro JWT antes del filtro estándar de Spring
            .addFilterBefore(
                    jwtAuthFilter, 
                    UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }
}
