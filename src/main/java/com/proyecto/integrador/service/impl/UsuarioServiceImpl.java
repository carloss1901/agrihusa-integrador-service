package com.proyecto.integrador.service.impl;

import com.proyecto.integrador.model.entity.UsuarioEntity;
import com.proyecto.integrador.model.entity.UsuarioRolEntity;
import com.proyecto.integrador.model.request.UsuarioRegistroRequest;
import com.proyecto.integrador.repository.RolRepository;
import com.proyecto.integrador.repository.UsuarioRepository;
import com.proyecto.integrador.repository.UsuarioRolRepository;
import com.proyecto.integrador.service.UsuarioService;
import com.proyecto.integrador.utils.MessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private static final String MSG_DNI_REGISTRADO = "El DNI ya está registrado";
    private static final String MSG_CORREO_REGISTRADO = "El correo ya está registrado";
    private static final String MSG_ROL_NO_EXISTE = "El rol no existe";
    private static final String MSG_USUARIO_REGISTRADO_OK = "Usuario registrado correctamente";
    private static final String CONTRASENIA_DEFAULT = "contraseña";

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final UsuarioRolRepository usuarioRolRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public ResponseEntity<Object> registrar(UsuarioRegistroRequest request) {
        String dni = request.getDni().trim();
        String correo = request.getCorreo().trim().toLowerCase();

        if (usuarioRepository.existsByDni(dni)) {
            return MessageResponse.setResponse(Boolean.FALSE, HttpStatus.CONFLICT, MSG_DNI_REGISTRADO);
        }
        if (usuarioRepository.existsByCorreo(correo)) {
            return MessageResponse.setResponse(Boolean.FALSE, HttpStatus.CONFLICT, MSG_CORREO_REGISTRADO);
        }

        var rol = rolRepository.findById(request.getRolId()).orElse(null);
        if (rol == null) {
            return MessageResponse.setResponse(Boolean.FALSE, HttpStatus.BAD_REQUEST, MSG_ROL_NO_EXISTE);
        }

        UsuarioEntity entity = new UsuarioEntity();
        entity.setDni(dni);
        entity.setUsuario(dni);
        entity.setNombres(request.getNombres().trim());
        entity.setApellidoPaterno(request.getApellidoPaterno().trim());
        entity.setApellidoMaterno(request.getApellidoMaterno().trim());
        entity.setCorreo(correo);
        entity.setTelefono(request.getTelefono() == null ? null : request.getTelefono().trim());
        entity.setContrasenia(passwordEncoder.encode(CONTRASENIA_DEFAULT));
        entity.setEsSistema(Boolean.FALSE);
        entity.setResetContrasenia(Boolean.TRUE);
        entity.setUltimoAcceso(null);
        entity.setActivo(Boolean.TRUE);
        entity.setFechaCreacion(LocalDate.now());
        entity.setFechaModificacion(null);

        UsuarioEntity usuarioGuardado = usuarioRepository.save(entity);

        UsuarioRolEntity usuarioRol = new UsuarioRolEntity();
        usuarioRol.setUsuarioId(usuarioGuardado.getUsuarioId());
        usuarioRol.setRolId(rol.getRolId());
        usuarioRol.setActivo(Boolean.TRUE);
        usuarioRol.setFechaAsignacion(LocalDate.now());
        usuarioRol.setFechaCreacion(LocalDate.now());
        usuarioRolRepository.save(usuarioRol);

        return MessageResponse.setResponse(Boolean.TRUE, HttpStatus.CREATED, MSG_USUARIO_REGISTRADO_OK);
    }
}
