package com.proyecto.integrador.model.response;

import com.proyecto.integrador.model.entity.BitacoraEntity;
import com.proyecto.integrador.model.projection.BitacoraProjection;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record BitacoraResponse(Integer bitacoraId, LocalDateTime fecha, Integer usuarioId, String modulo,
                               String accion, String entidad, Integer registroId, String detalle,
                               String resultado, Boolean activo, LocalDate fechaCreacion,
                               LocalDate fechaModificacion) {

    public static BitacoraResponse from(BitacoraProjection p) {
        return new BitacoraResponse(p.getBitacoraId(), p.getFecha(), p.getUsuarioId(), p.getModulo(),
                p.getAccion(), p.getEntidad(), p.getRegistroId(), p.getDetalle(), p.getResultado(),
                p.getActivo(), p.getFechaCreacion(), p.getFechaModificacion());
    }

    public static BitacoraResponse from(BitacoraEntity e) {
        return new BitacoraResponse(e.getBitacoraId(), e.getFecha(), e.getUsuarioId(), e.getModulo(),
                e.getAccion(), e.getEntidad(), e.getRegistroId(), e.getDetalle(), e.getResultado(),
                e.getActivo(), e.getFechaCreacion(), e.getFechaModificacion());
    }
}
