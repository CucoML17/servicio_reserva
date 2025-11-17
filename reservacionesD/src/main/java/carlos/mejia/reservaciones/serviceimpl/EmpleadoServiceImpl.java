package carlos.mejia.reservaciones.serviceimpl;



import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import carlos.mejia.reservaciones.dto.EmpleadoDto;
import carlos.mejia.reservaciones.dto.EmpleadoLocalUpdateRequest;
import carlos.mejia.reservaciones.dto.EmpleadoRegistroRequest;
import carlos.mejia.reservaciones.dto.EmpleadoUpdateRequest;
import carlos.mejia.reservaciones.dto.RegistroRequestDto;
import carlos.mejia.reservaciones.dto.UsuarioResponseDto;
import carlos.mejia.reservaciones.dto.UsuarioUpdateRequestDto;
import carlos.mejia.reservaciones.entity.Empleado;
import carlos.mejia.reservaciones.entity.Puesto;
import carlos.mejia.reservaciones.feign.AuthClientFeing;
import carlos.mejia.reservaciones.mapper.EmpleadoMapper;
import carlos.mejia.reservaciones.repository.EmpleadoRepository;
import carlos.mejia.reservaciones.repository.PuestoRepository;
import carlos.mejia.reservaciones.service.EmpleadoService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmpleadoServiceImpl implements EmpleadoService {
    
    private final EmpleadoRepository empleadoRepository;
    private final PuestoRepository puestoRepository; 
    
    private final AuthClientFeing authClient;

    //Guardar empleado
    @Override
    public EmpleadoDto save(EmpleadoDto dto) {
        //1. Obtener y validar la entidad Puesto usando el ID del DTO
        Puesto puesto = puestoRepository.findById(dto.getIdPuesto())
                .orElseThrow(() -> new RuntimeException("Puesto no encontrado con ID: " + dto.getIdPuesto()));
        
        //2. Crear Empleado
        Empleado empleado = EmpleadoMapper.mapToEmpleado(dto); 
        
        //3. Establecer la relación
        empleado.setPuesto(puesto); 
        
        Empleado savedEmpleado = empleadoRepository.save(empleado);
        return EmpleadoMapper.mapToEmpleadoDto(savedEmpleado);
    }
    
    @Override
    public List<EmpleadoDto> findAll() {
        return empleadoRepository.findAll().stream()
                .map(EmpleadoMapper::mapToEmpleadoDto)
                .collect(Collectors.toList());
    }
    
    //Encontrar por el puesto del empleado
    @Override
    public List<EmpleadoDto> findByPuestoId(Integer idPuesto) {
        
        //1.- El método para buscar del repository
        List<Empleado> empleados = empleadoRepository.findByPuesto_IdPuesto(idPuesto);
        
        //2. La lista de Entidades a una lista de DTOs
        return empleados.stream()
                .map(EmpleadoMapper::mapToEmpleadoDto)
                .collect(Collectors.toList());
    }    
    
    //Encontrar por id
    @Override
    public EmpleadoDto findById(Integer id) {
        Empleado empleado = empleadoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado con ID: " + id));
        return EmpleadoMapper.mapToEmpleadoDto(empleado);
    }
    
    //Actualizar empleado
    @Override
    public EmpleadoDto update(Integer id, EmpleadoDto dto) {
        Empleado empleado = empleadoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado con ID: " + id));
        
        //1. Actualizar el nombre
        empleado.setNombre(dto.getNombre());

        //2. Actualizar la relación del puesto
        if (!dto.getIdPuesto().equals(empleado.getPuesto().getIdPuesto())) {
             Puesto nuevoPuesto = puestoRepository.findById(dto.getIdPuesto())
                    .orElseThrow(() -> new RuntimeException("Puesto no encontrado con ID: " + dto.getIdPuesto()));
             empleado.setPuesto(nuevoPuesto);
        }

        Empleado updatedEmpleado = empleadoRepository.save(empleado);
        return EmpleadoMapper.mapToEmpleadoDto(updatedEmpleado);
    }
    
    @Override
    public void delete(Integer id) {
        empleadoRepository.deleteById(id);
    }
    
    //Filtros estandarizados:
    
    @Override
    public List<EmpleadoDto> filtrarEmpleados(String nombreBuscar, Integer idPuestoBuscar) {
        
        List<Empleado> empleados;

        // 1. Caso: Filtrar por Nombre Y Puesto
        if (nombreBuscar != null && !nombreBuscar.isEmpty() && idPuestoBuscar != null) {
            empleados = empleadoRepository.findByNombreContainingIgnoreCaseAndPuesto_IdPuesto(nombreBuscar, idPuestoBuscar);
            
        // 2. Caso: Filtrar solo por Nombre
        } else if (nombreBuscar != null && !nombreBuscar.isEmpty()) {
            empleados = empleadoRepository.findByNombreContainingIgnoreCase(nombreBuscar);
            
        // 3. Caso: Filtrar solo por Puesto (reutilizando tu método existente)
        } else if (idPuestoBuscar != null) {
            empleados = empleadoRepository.findByPuesto_IdPuesto(idPuestoBuscar);

        // 4. Caso: Sin filtros (Devolver todos)
        } else {
            empleados = empleadoRepository.findAll();
        }
        
        // 5. Mapear y retornar
        return empleados.stream()
                .map(EmpleadoMapper::mapToEmpleadoDto)
                .collect(Collectors.toList());
    }    
    
    @Override
    public EmpleadoDto registerEmpleadoWithOptionalUser(EmpleadoRegistroRequest request) {
        
        Integer idUsuarioAsignado = null;
        
        //1. Lógica Condicional: Registrar Usuario solo si se provee username y password.
        if (request.getUsername() != null && !request.getUsername().isEmpty() && request.getPassword() != null) {
            
            //a. Validación de Puesto/Perfil: Asegurarse de que el puesto requiere un perfil
            if (request.getIdPerfil() == null) {
                 
                 throw new RuntimeException("Si se proveen credenciales, se debe especificar el ID de Perfil (idPerfil).");
            }
            
            //b. Mapear al DTO que auth-service espera
            RegistroRequestDto authRequest = new RegistroRequestDto();
            authRequest.setUsername(request.getUsername());
            authRequest.setPassword(request.getPassword());
            authRequest.setIdPerfil(request.getIdPerfil()); 
            
            //c. Llamar a auth-service para crear el usuario
            UsuarioResponseDto userResponse = authClient.registerStaff(authRequest);
            idUsuarioAsignado = userResponse.getId(); // Capturar el ID de usuario
        }
        
        //2. Crear el DTO del Empleado
        EmpleadoDto empleadoDto = new EmpleadoDto();
        empleadoDto.setNombre(request.getNombre());
        empleadoDto.setIdPuesto(request.getIdPuesto());
        
        //3. Asignar el idUsuario (será NULL si no se creó, o el ID devuelto)
        empleadoDto.setIdUsuario(idUsuarioAsignado); 

        //4. Reutilizar el método de guardado existente (save)
        return save(empleadoDto);
    }   
    
    
    
    
    @Override
    public EmpleadoDto getEmpleadoByUserId(Integer idUsuario) { // <-- ¡NUEVO!
        
        // 1. Buscar la entidad por el idUsuario.
        // Usamos orElseThrow porque el Frontend solo pedirá el perfil
        // si el usuario tiene un rol asociado a un Empleado (Admin, Cajero, etc.).
        Empleado empleado = empleadoRepository.findByIdUsuario(idUsuario)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado con ID de Usuario: " + idUsuario));
        
        // 2. Mapear a DTO y retornar
        return EmpleadoMapper.mapToEmpleadoDto(empleado);
    }    
    
    
    
    
    
    @Override
    public EmpleadoDto updateEmpleadoWithOptionalUser(Integer idEmpleado, EmpleadoUpdateRequest request) { // <-- ¡NUEVO!

        // 1. Buscar el empleado existente para obtener su ID de Usuario
        Empleado empleado = empleadoRepository.findById(idEmpleado)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado con ID: " + idEmpleado));

        // 2. Actualizar la Entidad Empleado
        empleado.setNombre(request.getNombre());
        
        // Asignar el nuevo puesto (asumiendo que PuestoRepository.findById es manejado)
        Puesto nuevoPuesto = puestoRepository.findById(request.getIdPuesto())
                .orElseThrow(() -> new RuntimeException("Puesto no encontrado con ID: " + request.getIdPuesto()));
        empleado.setPuesto(nuevoPuesto); 
        
        // 3. Lógica Condicional: Actualizar Usuario solo si el empleado tiene un idUsuario asignado
        Integer idUsuarioActualizado = empleado.getIdUsuario();
        
        // CRÍTICO: El empleado debe tener un ID de usuario asociado para poder actualizarlo
        if (idUsuarioActualizado != null) {
            
            // Verificamos que al menos se proporcionen los campos para actualizar
            if (request.getUsername() != null && request.getIdPerfil() != null) {
                
                // Mapear al DTO que auth-service espera
                UsuarioUpdateRequestDto authUpdateRequest = new UsuarioUpdateRequestDto();
                authUpdateRequest.setUsername(request.getUsername());
                authUpdateRequest.setIdPerfil(request.getIdPerfil());
                
                // 4. Llamar a auth-service para actualizar el usuario (Requiere TOKEN)
                try {
                    UsuarioResponseDto userResponse = authClient.updateUsuarioSinContrasenia(
                        idUsuarioActualizado, authUpdateRequest);
                    
                    // Si la llamada es exitosa, el ID del usuario no cambia, pero el perfil y username sí.
                    // No necesitamos actualizar el idUsuario del Empleado, solo confirmar que funcionó.
                    
                } catch (Exception e) {
                    throw new RuntimeException("Error al actualizar el usuario/perfil en authservice: " + e.getMessage());
                }
            }
        }
        
        // 5. Guardar la Entidad Empleado (Actualiza nombre y puesto)
        Empleado empleadoActualizado = empleadoRepository.save(empleado);
        
        // 6. Mapear y devolver el DTO
        return EmpleadoMapper.mapToEmpleadoDto(empleadoActualizado); // Asume que este mapper ya existe
        
        
    }    
    
    @Override
    public EmpleadoDto updateEmpleadoLocalData(Integer idEmpleado, EmpleadoLocalUpdateRequest request) { // <-- ¡NUEVO!
        
        // 1. Buscar el empleado existente
        Empleado empleado = empleadoRepository.findById(idEmpleado)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado con ID: " + idEmpleado));

        // 2. Buscar el nuevo Puesto (debe existir)
        Puesto nuevoPuesto = puestoRepository.findById(request.getIdPuesto())
                .orElseThrow(() -> new RuntimeException("Puesto no encontrado con ID: " + request.getIdPuesto()));
        
        // 3. Aplicar los cambios de datos de negocio
        empleado.setNombre(request.getNombre());
        empleado.setPuesto(nuevoPuesto); 
        
        // 4. Actualizar el ID de Usuario. 
        // Nota: Asumimos que el idUsuario enviado es válido y existe en authservice.
        // Si el idUsuario es nulo, se desvincula de cualquier cuenta de usuario.
        empleado.setIdUsuario(request.getIdUsuario()); 
        
        // 5. Guardar los cambios
        Empleado empleadoActualizado = empleadoRepository.save(empleado);
        
        // 6. Mapear y devolver el DTO
        return EmpleadoMapper.mapToEmpleadoDto(empleadoActualizado); // Asume que este mapper ya existe
    }    
    
    
}