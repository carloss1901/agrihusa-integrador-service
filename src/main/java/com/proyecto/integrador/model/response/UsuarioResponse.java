package com.proyecto.integrador.model.response;

import com.proyecto.integrador.model.entity.UsuarioEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record UsuarioResponse(
        Integer usuarioId,
        String dni,
        String usuario,
        String nombres,
        String apellidoPaterno,
        String apellidoMaterno,
        String correo,
        String telefono,
        Integer rolId,
        Boolean esSistema,
        Boolean resetContrasenia,
        LocalDateTime ultimoAcceso,
        Boolean activo,
        LocalDate fechaCreacion,
        LocalDate fechaModificacion
) {

    public static UsuarioResponse from(
            UsuarioEntity entity,
            Integer rolId
    ) {
        return new UsuarioResponse(
                entity.getUsuarioId(),
                entity.getDni(),
                entity.getUsuario(),
                entity.getNombres(),
                entity.getApellidoPaterno(),
                entity.getApellidoMaterno(),
                entity.getCorreo(),
                entity.getTelefono(),
                rolId,
                entity.getEsSistema(),
                entity.getResetContrasenia(),
                entity.getUltimoAcceso(),
                entity.getActivo(),
                entity.getFechaCreacion(),
                entity.getFechaModificacion()
        );
    }
}