package com.proyecto.integrador.model.response;

import com.proyecto.integrador.model.entity.BitacoraEntity;
import com.proyecto.integrador.model.projection.BitacoraProjection;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record BitacoraResponse(
        Integer bitacoraId,
        LocalDateTime fecha,
        Integer usuarioId,
        String nombreUsuario,
        String modulo,
        String accion,
        String entidad,
        Integer registroId,
        String detalle,
        String resultado,
        Boolean activo,
        LocalDate fechaCreacion,
        LocalDate fechaModificacion
) {

    public static BitacoraResponse from(
            BitacoraProjection projection
    ) {
        return new BitacoraResponse(
                projection.getBitacoraId(),
                projection.getFecha(),
                projection.getUsuarioId(),
                projection.getNombreUsuario(),
                projection.getModulo(),
                projection.getAccion(),
                projection.getEntidad(),
                projection.getRegistroId(),
                projection.getDetalle(),
                projection.getResultado(),
                projection.getActivo(),
                projection.getFechaCreacion(),
                projection.getFechaModificacion()
        );
    }

    public static BitacoraResponse from(
            BitacoraEntity entity
    ) {
        return new BitacoraResponse(
                entity.getBitacoraId(),
                entity.getFecha(),
                entity.getUsuarioId(),
                null,
                entity.getModulo(),
                entity.getAccion(),
                entity.getEntidad(),
                entity.getRegistroId(),
                entity.getDetalle(),
                entity.getResultado(),
                entity.getActivo(),
                entity.getFechaCreacion(),
                entity.getFechaModificacion()
        );
    }
}