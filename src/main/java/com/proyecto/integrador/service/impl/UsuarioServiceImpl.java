package com.proyecto.integrador.service.impl;

import com.proyecto.integrador.model.entity.UsuarioEntity;
import com.proyecto.integrador.model.entity.UsuarioRolEntity;
import com.proyecto.integrador.model.request.UsuarioRegistroRequest;
import com.proyecto.integrador.repository.RolRepository;
import com.proyecto.integrador.repository.UsuarioRepository;
import com.proyecto.integrador.repository.UsuarioRolRepository;
import com.proyecto.integrador.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final UsuarioRolRepository usuarioRolRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UsuarioEntity registrar(UsuarioRegistroRequest request) {
        String dni = request.getDni().trim();
        String usuario = request.getUsuario().trim().toUpperCase();
        String correo = request.getCorreo().trim().toLowerCase();

        if (usuarioRepository.existsByDni(dni)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El DNI ya está registrado");
        }
        if (usuarioRepository.existsByUsuario(usuario)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El usuario ya está registrado");
        }
        if (usuarioRepository.existsByCorreo(correo)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El correo ya está registrado");
        }

        var rol = rolRepository.findById(request.getRolId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "El rol no existe"));

        UsuarioEntity entity = new UsuarioEntity();
        entity.setDni(dni);
        entity.setUsuario(usuario);
        entity.setNombres(request.getNombres().trim());
        entity.setApellidoPaterno(request.getApellidoPaterno().trim());
        entity.setApellidoMaterno(request.getApellidoMaterno().trim());
        entity.setCorreo(correo);
        entity.setTelefono(request.getTelefono() == null ? null : request.getTelefono().trim());
        entity.setContrasenia(passwordEncoder.encode(request.getContrasenia()));
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

        return usuarioGuardado;
    }
}
