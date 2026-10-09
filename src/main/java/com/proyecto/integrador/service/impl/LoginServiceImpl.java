package com.proyecto.integrador.service.impl;

import com.proyecto.integrador.model.entity.UsuarioEntity;
import com.proyecto.integrador.model.entity.UsuarioRolEntity;
import com.proyecto.integrador.model.entity.ModuloEntity;
import com.proyecto.integrador.model.entity.PermisoEntity;
import com.proyecto.integrador.model.entity.RolPermisoEntity;
import com.proyecto.integrador.model.request.LoginRequest;
import com.proyecto.integrador.repository.ModuloRepository;
import com.proyecto.integrador.repository.PermisoRepository;
import com.proyecto.integrador.repository.RolRepository;
import com.proyecto.integrador.repository.RolPermisoRepository;
import com.proyecto.integrador.repository.UsuarioRepository;
import com.proyecto.integrador.repository.UsuarioRolRepository;
import com.proyecto.integrador.service.JwtService;
import com.proyecto.integrador.service.LoginService;
import com.proyecto.integrador.utils.MessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private static final String MSG_CREDENCIALES_INVALIDAS = "Usuario o contraseña incorrectos";
    private static final String MSG_LOGIN_CORRECTO = "Inicio de sesión correcto";

    private final UsuarioRepository usuarioRepository;
    private final UsuarioRolRepository usuarioRolRepository;
    private final RolRepository rolRepository;
    private final RolPermisoRepository rolPermisoRepository;
    private final ModuloRepository moduloRepository;
    private final PermisoRepository permisoRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public ResponseEntity<MessageResponse> login(LoginRequest request) {
        String usuarioIngresado = request.getUsuario().trim();
        UsuarioEntity usuario = usuarioRepository.findByUsuarioAndActivoTrue(usuarioIngresado).orElse(null);

        if (usuario == null || !passwordEncoder.matches(request.getContrasenia(), usuario.getContrasenia())) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(MessageResponse.body(
                            Boolean.FALSE,
                            HttpStatus.UNAUTHORIZED,
                            MSG_CREDENCIALES_INVALIDAS,
                            null
                    ));
        }

        usuario.setUltimoAcceso(
                LocalDateTime.now()
        );

        usuarioRepository.save(usuario);

        List<Integer> rolesIds = usuarioRolRepository.findAllByUsuarioIdAndActivoTrue(usuario.getUsuarioId())
                .stream()
                .map(UsuarioRolEntity::getRolId)
                .toList();

        List<Map<String, Object>> roles = new ArrayList<>();
        if (!rolesIds.isEmpty()) {
            roles = rolRepository.findAllByRolIdInAndActivoTrue(rolesIds)
                    .stream()
                    .map(rol -> {
                        Map<String, Object> role = new HashMap<>();
                        role.put("rolId", rol.getRolId());
                        role.put("nombre", rol.getNombre());
                        role.put("modulos", construirModulos(rol.getRolId()));
                        return role;
                    })
                    .toList();
        }

        String token = jwtService.generateToken(usuario, roles);
        return ResponseEntity
                .ok(MessageResponse.body(
                        Boolean.TRUE,
                        HttpStatus.OK,
                        MSG_LOGIN_CORRECTO,
                        token
                ));
    }

    private List<Map<String, Object>> construirModulos(Integer rolId) {
        List<RolPermisoEntity> relaciones = rolPermisoRepository.findAllByRolId(rolId)
                .stream()
                .filter(relacion -> Boolean.TRUE.equals(relacion.getActivo()))
                .toList();

        Map<Integer, ModuloEntity> modulos = new HashMap<>();
        Map<Integer, PermisoEntity> permisos = new HashMap<>();

        moduloRepository.findAllById(relaciones.stream()
                        .map(RolPermisoEntity::getModuloId)
                        .distinct()
                        .toList())
                .stream()
                .filter(modulo -> Boolean.TRUE.equals(modulo.getActivo()))
                .forEach(modulo -> modulos.put(modulo.getModuloId(), modulo));

        permisoRepository.findAllById(relaciones.stream()
                        .map(RolPermisoEntity::getPermisoId)
                        .distinct()
                        .toList())
                .stream()
                .filter(permiso -> Boolean.TRUE.equals(permiso.getActivo()))
                .forEach(permiso -> permisos.put(permiso.getPermisoId(), permiso));

        Map<Integer, Map<String, Object>> modulosPorId = new LinkedHashMap<>();
        for (RolPermisoEntity relacion : relaciones) {
            ModuloEntity modulo = modulos.get(relacion.getModuloId());
            PermisoEntity permiso = permisos.get(relacion.getPermisoId());
            if (modulo == null || permiso == null) {
                continue;
            }

            Map<String, Object> moduloToken = modulosPorId.computeIfAbsent(modulo.getModuloId(), id -> {
                Map<String, Object> datos = new LinkedHashMap<>();
                datos.put("moduloId", modulo.getModuloId());
                datos.put("codigo", modulo.getCodigo());
                datos.put("nombre", modulo.getNombre());
                datos.put("permisos", new ArrayList<Map<String, Object>>());
                return datos;
            });

            @SuppressWarnings("unchecked")
            List<Map<String, Object>> permisosToken =
                    (List<Map<String, Object>>) moduloToken.get("permisos");
            Map<String, Object> permisoToken = new LinkedHashMap<>();
            permisoToken.put("permisoId", permiso.getPermisoId());
            permisoToken.put("accion", permiso.getAccion());
            permisoToken.put("descripcion", permiso.getDescripcion());
            permisosToken.add(permisoToken);
        }

        return new ArrayList<>(modulosPorId.values());
    }
}
