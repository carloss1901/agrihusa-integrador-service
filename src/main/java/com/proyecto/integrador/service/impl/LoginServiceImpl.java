package com.proyecto.integrador.service.impl;

import com.proyecto.integrador.model.entity.UsuarioEntity;
import com.proyecto.integrador.model.entity.UsuarioRolEntity;
import com.proyecto.integrador.model.request.LoginRequest;
import com.proyecto.integrador.repository.RolRepository;
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

import java.util.ArrayList;
import java.util.HashMap;
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
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public ResponseEntity<Object> login(LoginRequest request) {
        String usuarioIngresado = request.getUsuario().trim();
        UsuarioEntity usuario = usuarioRepository.findByUsuarioAndActivoTrue(usuarioIngresado).orElse(null);

        if (usuario == null || !passwordEncoder.matches(request.getContrasenia(), usuario.getContrasenia())) {
            return MessageResponse.setResponse(Boolean.FALSE, HttpStatus.UNAUTHORIZED, MSG_CREDENCIALES_INVALIDAS);
        }

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
                        return role;
                    })
                    .toList();
        }

        String token = jwtService.generateToken(usuario, roles);
        return MessageResponse.setResponse(Boolean.TRUE, HttpStatus.OK, MSG_LOGIN_CORRECTO, token);
    }
}
