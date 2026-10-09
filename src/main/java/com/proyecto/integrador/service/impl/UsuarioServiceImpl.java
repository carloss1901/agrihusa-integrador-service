package com.proyecto.integrador.service.impl;

import com.proyecto.integrador.model.entity.UsuarioEntity;
import com.proyecto.integrador.model.entity.UsuarioRolEntity;
import com.proyecto.integrador.model.request.UsuarioRegistroRequest;
import com.proyecto.integrador.model.request.CambiarContraseniaRequest;
import com.proyecto.integrador.repository.RolRepository;
import com.proyecto.integrador.repository.UsuarioRepository;
import com.proyecto.integrador.repository.UsuarioRolRepository;
import com.proyecto.integrador.service.UsuarioService;
import com.proyecto.integrador.security.JwtData;
import com.proyecto.integrador.utils.MessageResponse;
import com.proyecto.integrador.utils.CustomPage;
import com.proyecto.integrador.model.response.UsuarioResponse;
import com.proyecto.integrador.model.mapper.GlobalMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private static final String MSG_DNI_REGISTRADO = "El DNI ya está registrado";
    private static final String MSG_CORREO_REGISTRADO = "El correo ya está registrado";
    private static final String MSG_ROL_NO_EXISTE = "El rol no existe";
    private static final String MSG_USUARIO_REGISTRADO_OK = "Usuario registrado correctamente";
    private static final String MSG_USUARIO_ACTUALIZADO_OK = "Usuario actualizado correctamente";
    private static final String MSG_USUARIO_ACTIVADO = "Usuario activado correctamente";
    private static final String MSG_USUARIO_DESACTIVADO = "Usuario desactivado correctamente";
    private static final String MSG_USUARIO_NO_ENCONTRADO = "El usuario no existe o está inactivo";
    private static final String MSG_CONTRASENIA_ACTUAL_INVALIDA = "La contraseña actual es incorrecta";
    private static final String MSG_CONTRASENIAS_NO_COINCIDEN = "La nueva contraseña y su confirmación no coinciden";
    private static final String MSG_CONTRASENIA_ACTUAL = "La nueva contraseña debe ser diferente a la actual";
    private static final String MSG_CONTRASENIA_ACTUALIZADA = "Contraseña actualizada correctamente";
    private static final String CONTRASENIA_DEFAULT = "contraseña";

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final UsuarioRolRepository usuarioRolRepository;
    private final PasswordEncoder passwordEncoder;
    private final GlobalMapper globalMapper;

    @Autowired
    public UsuarioServiceImpl(UsuarioRepository usuarioRepository,
                              RolRepository rolRepository,
                              UsuarioRolRepository usuarioRolRepository,
                              PasswordEncoder passwordEncoder,
                              GlobalMapper globalMapper) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.usuarioRolRepository = usuarioRolRepository;
        this.passwordEncoder = passwordEncoder;
        this.globalMapper = globalMapper;
    }

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository,
                              RolRepository rolRepository,
                              UsuarioRolRepository usuarioRolRepository,
                              PasswordEncoder passwordEncoder) {
        this(usuarioRepository, rolRepository, usuarioRolRepository, passwordEncoder, new GlobalMapper());
    }

    @Override
    @Transactional
    public ResponseEntity<MessageResponse> registrar(UsuarioRegistroRequest request) {
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

    @Override
    @Transactional(readOnly = true)
    public CustomPage<UsuarioResponse> listar(String texto, Boolean activo, Pageable pageable) {
        Page<UsuarioResponse> usuarios = usuarioRepository.listar(texto, activo, pageable)
                .map(projection -> globalMapper.map(projection, UsuarioResponse.class));
        return new CustomPage<>(usuarios);
    }

    @Override
    @Transactional
    public ResponseEntity<MessageResponse> actualizar(UsuarioRegistroRequest request) {
        UsuarioEntity entity = usuarioRepository.findById(request.getUsuarioId()).orElse(null);
        if (entity == null) {
            return MessageResponse.setResponse(Boolean.FALSE, HttpStatus.NOT_FOUND, MSG_USUARIO_NO_ENCONTRADO);
        }

        String dni = request.getDni().trim();
        String correo = request.getCorreo().trim().toLowerCase();
        if (!dni.equals(entity.getDni()) && usuarioRepository.existsByDni(dni)) {
            return MessageResponse.setResponse(Boolean.FALSE, HttpStatus.CONFLICT, MSG_DNI_REGISTRADO);
        }
        if (!correo.equalsIgnoreCase(entity.getCorreo()) && usuarioRepository.existsByCorreo(correo)) {
            return MessageResponse.setResponse(Boolean.FALSE, HttpStatus.CONFLICT, MSG_CORREO_REGISTRADO);
        }

        var rol = rolRepository.findById(request.getRolId()).orElse(null);
        if (rol == null) {
            return MessageResponse.setResponse(Boolean.FALSE, HttpStatus.BAD_REQUEST, MSG_ROL_NO_EXISTE);
        }

        entity.setDni(dni);
        entity.setUsuario(dni);
        entity.setNombres(request.getNombres().trim());
        entity.setApellidoPaterno(request.getApellidoPaterno().trim());
        entity.setApellidoMaterno(request.getApellidoMaterno().trim());
        entity.setCorreo(correo);
        entity.setTelefono(request.getTelefono() == null ? null : request.getTelefono().trim());
        entity.setFechaModificacion(LocalDate.now());
        usuarioRepository.save(entity);

        usuarioRolRepository.findAllByUsuarioIdAndActivoTrue(entity.getUsuarioId())
                .forEach(relacion -> relacion.setActivo(Boolean.FALSE));
        UsuarioRolEntity usuarioRol = new UsuarioRolEntity();
        usuarioRol.setUsuarioId(entity.getUsuarioId());
        usuarioRol.setRolId(rol.getRolId());
        usuarioRol.setActivo(Boolean.TRUE);
        usuarioRol.setFechaAsignacion(LocalDate.now());
        usuarioRol.setFechaCreacion(LocalDate.now());
        usuarioRolRepository.save(usuarioRol);

        return MessageResponse.setResponse(Boolean.TRUE, HttpStatus.OK, MSG_USUARIO_ACTUALIZADO_OK);
    }

    @Override
    @Transactional
    public ResponseEntity<MessageResponse> cambiarEstado(Integer usuarioId, Boolean activo) {
        UsuarioEntity entity = usuarioRepository.findById(usuarioId).orElse(null);
        if (entity == null) {
            return MessageResponse.setResponse(Boolean.FALSE, HttpStatus.NOT_FOUND, MSG_USUARIO_NO_ENCONTRADO);
        }
        entity.setActivo(activo);
        entity.setFechaModificacion(LocalDate.now());
        usuarioRepository.save(entity);
        String mensaje = Boolean.TRUE.equals(activo) ? MSG_USUARIO_ACTIVADO : MSG_USUARIO_DESACTIVADO;
        return MessageResponse.setResponse(Boolean.TRUE, HttpStatus.OK, mensaje);
    }

    @Override
    @Transactional
    public ResponseEntity<MessageResponse> cambiarContrasenia(CambiarContraseniaRequest request) {
        Integer usuarioId = JwtData.getUsuarioId();
        UsuarioEntity usuario = usuarioId == null
                ? null
                : usuarioRepository.findById(usuarioId).orElse(null);

        if (usuario == null || !Boolean.TRUE.equals(usuario.getActivo())) {
            return MessageResponse.setResponse(Boolean.FALSE, HttpStatus.NOT_FOUND, MSG_USUARIO_NO_ENCONTRADO);
        }
        if (!passwordEncoder.matches(request.getContraseniaActual(), usuario.getContrasenia())) {
            return MessageResponse.setResponse(Boolean.FALSE, HttpStatus.BAD_REQUEST, MSG_CONTRASENIA_ACTUAL_INVALIDA);
        }
        if (!request.getNuevaContrasenia().equals(request.getConfirmarContrasenia())) {
            return MessageResponse.setResponse(Boolean.FALSE, HttpStatus.BAD_REQUEST, MSG_CONTRASENIAS_NO_COINCIDEN);
        }
        if (passwordEncoder.matches(request.getNuevaContrasenia(), usuario.getContrasenia())) {
            return MessageResponse.setResponse(Boolean.FALSE, HttpStatus.BAD_REQUEST, MSG_CONTRASENIA_ACTUAL);
        }

        usuario.setContrasenia(passwordEncoder.encode(request.getNuevaContrasenia()));
        usuario.setResetContrasenia(Boolean.FALSE);
        usuarioRepository.save(usuario);

        return MessageResponse.setResponse(Boolean.TRUE, HttpStatus.OK, MSG_CONTRASENIA_ACTUALIZADA);
    }
}

