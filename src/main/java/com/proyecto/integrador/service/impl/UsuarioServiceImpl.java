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
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.proyecto.integrador.model.response.UsuarioResponse;
import com.proyecto.integrador.utils.CustomPage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDate;
import com.proyecto.integrador.model.request.UsuarioActualizarRequest;
import com.proyecto.integrador.model.request.PerfilUsuarioActualizarRequest;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private static final String MSG_DNI_REGISTRADO = "El DNI ya está registrado";
    private static final String MSG_CORREO_REGISTRADO = "El correo ya está registrado";
    private static final String MSG_ROL_NO_EXISTE = "El rol no existe";
    private static final String MSG_USUARIO_REGISTRADO_OK = "Usuario registrado correctamente";
    private static final String MSG_USUARIO_NO_ENCONTRADO = "El usuario no existe o está inactivo";
    private static final String MSG_CONTRASENIA_ACTUAL_INVALIDA = "La contraseña actual es incorrecta";
    private static final String MSG_CONTRASENIAS_NO_COINCIDEN = "La nueva contraseña y su confirmación no coinciden";
    private static final String MSG_CONTRASENIA_ACTUAL = "La nueva contraseña debe ser diferente a la actual";
    private static final String MSG_CONTRASENIA_ACTUALIZADA = "Contraseña actualizada correctamente";
    private static final String MSG_USUARIO_ACTUALIZADO_OK = "Usuario actualizado correctamente";
    private static final String MSG_USUARIO_ESTADO_OK = "Estado del usuario actualizado correctamente";
    private static final String MSG_USUARIO_SISTEMA = "No se puede cambiar el estado de un usuario del sistema";
    private static final String MSG_PERFIL_ACTUALIZADO_OK = "Perfil actualizado correctamente";

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final UsuarioRolRepository usuarioRolRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public CustomPage<UsuarioResponse> listarUsuarios(
            String texto,
            Integer rolId,
            Boolean activo,
            Pageable pageable
    ) {
        String textoBusqueda =
                texto == null || texto.isBlank()
                        ? null
                        : texto.trim();

        Page<UsuarioResponse> usuarios =
                usuarioRepository
                        .listarUsuarios(
                                textoBusqueda,
                                rolId,
                                activo,
                                pageable
                        )
                        .map(usuario -> {
                            Integer usuarioRolId =
                                    usuarioRolRepository
                                            .findAllByUsuarioIdAndActivoTrue(
                                                    usuario.getUsuarioId()
                                            )
                                            .stream()
                                            .findFirst()
                                            .map(
                                                    UsuarioRolEntity::getRolId
                                            )
                                            .orElse(null);

                            return UsuarioResponse.from(
                                    usuario,
                                    usuarioRolId
                            );
                        });

        return new CustomPage<>(usuarios);
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioResponse obtenerPorId(
            Integer usuarioId
    ) {
        UsuarioEntity usuario =
                usuarioRepository
                        .findById(usuarioId)
                        .orElse(null);

        if (usuario == null) {
            return null;
        }

        Integer rolId =
                usuarioRolRepository
                        .findAllByUsuarioIdAndActivoTrue(
                                usuarioId
                        )
                        .stream()
                        .findFirst()
                        .map(UsuarioRolEntity::getRolId)
                        .orElse(null);

        return UsuarioResponse.from(
                usuario,
                rolId
        );
    }

    @Override
    @Transactional
    public ResponseEntity<Object> actualizar(
            UsuarioActualizarRequest request
    ) {
        UsuarioEntity usuario =
                usuarioRepository
                        .findById(request.getUsuarioId())
                        .orElse(null);

        if (usuario == null) {
            return MessageResponse.setResponse(
                    Boolean.FALSE,
                    HttpStatus.NOT_FOUND,
                    MSG_USUARIO_NO_ENCONTRADO
            );
        }

        String dni = request.getDni().trim();
        String correo =
                request.getCorreo().trim().toLowerCase();

        if (
                usuarioRepository
                        .existsByDniAndUsuarioIdNot(
                                dni,
                                usuario.getUsuarioId()
                        )
        ) {
            return MessageResponse.setResponse(
                    Boolean.FALSE,
                    HttpStatus.CONFLICT,
                    MSG_DNI_REGISTRADO
            );
        }

        if (
                usuarioRepository
                        .existsByCorreoAndUsuarioIdNot(
                                correo,
                                usuario.getUsuarioId()
                        )
        ) {
            return MessageResponse.setResponse(
                    Boolean.FALSE,
                    HttpStatus.CONFLICT,
                    MSG_CORREO_REGISTRADO
            );
        }

        var rol =
                rolRepository
                        .findById(request.getRolId())
                        .orElse(null);

        if (rol == null) {
            return MessageResponse.setResponse(
                    Boolean.FALSE,
                    HttpStatus.BAD_REQUEST,
                    MSG_ROL_NO_EXISTE
            );
        }

        if (!Boolean.TRUE.equals(usuario.getEsSistema())) {
            usuario.setDni(dni);
            usuario.setUsuario(dni);
            usuario.setActivo(request.getActivo());
        }

        usuario.setNombres(request.getNombres().trim());
        usuario.setApellidoPaterno(
                request.getApellidoPaterno().trim()
        );
        usuario.setApellidoMaterno(
                request.getApellidoMaterno().trim()
        );
        usuario.setCorreo(correo);
        usuario.setTelefono(
                request.getTelefono() == null
                        ? null
                        : request.getTelefono().trim()
        );

        usuarioRepository.save(usuario);

        if (!Boolean.TRUE.equals(usuario.getEsSistema())) {
            var relacionesActuales =
                    usuarioRolRepository
                            .findAllByUsuarioIdAndActivoTrue(
                                    usuario.getUsuarioId()
                            );

            Integer rolActualId =
                    relacionesActuales
                            .stream()
                            .findFirst()
                            .map(UsuarioRolEntity::getRolId)
                            .orElse(null);

            if (!request.getRolId().equals(rolActualId)) {
                relacionesActuales.forEach(
                        relacion ->
                                relacion.setActivo(Boolean.FALSE)
                );

                usuarioRolRepository.saveAll(
                        relacionesActuales
                );

                UsuarioRolEntity nuevaRelacion =
                        new UsuarioRolEntity();

                nuevaRelacion.setUsuarioId(
                        usuario.getUsuarioId()
                );
                nuevaRelacion.setRolId(
                        request.getRolId()
                );
                nuevaRelacion.setActivo(Boolean.TRUE);
                nuevaRelacion.setFechaAsignacion(
                        LocalDate.now()
                );
                nuevaRelacion.setFechaCreacion(
                        LocalDate.now()
                );

                usuarioRolRepository.save(nuevaRelacion);
            }
        }

        return MessageResponse.setResponse(
                Boolean.TRUE,
                HttpStatus.OK,
                MSG_USUARIO_ACTUALIZADO_OK
        );
    }

    @Override
    @Transactional
    public ResponseEntity<Object> cambiarEstado(
            Integer usuarioId,
            Boolean activo
    ) {
        UsuarioEntity usuario =
                usuarioRepository
                        .findById(usuarioId)
                        .orElse(null);

        if (usuario == null) {
            return MessageResponse.setResponse(
                    Boolean.FALSE,
                    HttpStatus.NOT_FOUND,
                    MSG_USUARIO_NO_ENCONTRADO
            );
        }

        if (Boolean.TRUE.equals(usuario.getEsSistema())) {
            return MessageResponse.setResponse(
                    Boolean.FALSE,
                    HttpStatus.BAD_REQUEST,
                    MSG_USUARIO_SISTEMA
            );
        }

        usuario.setActivo(activo);
        usuarioRepository.save(usuario);

        return MessageResponse.setResponse(
                Boolean.TRUE,
                HttpStatus.OK,
                MSG_USUARIO_ESTADO_OK
        );
    }

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
        entity.setContrasenia(
                passwordEncoder.encode(
                        request.getContrasenia()
                )
        );
        entity.setEsSistema(Boolean.FALSE);
        entity.setResetContrasenia(Boolean.TRUE);
        entity.setUltimoAcceso(null);
        entity.setActivo(request.getActivo());
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
    @Transactional
    public ResponseEntity<Object> actualizarPerfil(
            PerfilUsuarioActualizarRequest request
    ) {
        Integer usuarioId =
                JwtData.getUsuarioId();

        UsuarioEntity usuario =
                usuarioId == null
                        ? null
                        : usuarioRepository
                        .findById(usuarioId)
                        .orElse(null);

        if (
                usuario == null ||
                        !Boolean.TRUE.equals(usuario.getActivo())
        ) {
            return MessageResponse.setResponse(
                    Boolean.FALSE,
                    HttpStatus.NOT_FOUND,
                    MSG_USUARIO_NO_ENCONTRADO
            );
        }

        String correo =
                request.getCorreo()
                        .trim()
                        .toLowerCase();

        if (
                usuarioRepository
                        .existsByCorreoAndUsuarioIdNot(
                                correo,
                                usuarioId
                        )
        ) {
            return MessageResponse.setResponse(
                    Boolean.FALSE,
                    HttpStatus.CONFLICT,
                    MSG_CORREO_REGISTRADO
            );
        }

        usuario.setNombres(
                request.getNombres().trim()
        );
        usuario.setApellidoPaterno(
                request.getApellidoPaterno().trim()
        );
        usuario.setApellidoMaterno(
                request.getApellidoMaterno().trim()
        );
        usuario.setCorreo(correo);
        usuario.setTelefono(
                request.getTelefono() == null
                        ? null
                        : request.getTelefono().trim()
        );

        usuarioRepository.save(usuario);

        return MessageResponse.setResponse(
                Boolean.TRUE,
                HttpStatus.OK,
                MSG_PERFIL_ACTUALIZADO_OK
        );
    }

    @Override
    @Transactional
    public ResponseEntity<Object> cambiarContrasenia(CambiarContraseniaRequest request) {
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
