package com.proyecto.integrador.model.response;

import com.proyecto.integrador.model.entity.SituacionEntity;
import com.proyecto.integrador.model.projection.SituacionProjection;

public record SituacionResponse(
        Integer situacionId,
        String descripcion,
        Boolean activo,
        String estadoDsc
) {

    public static SituacionResponse from(SituacionProjection projection) {
        return new SituacionResponse(
                projection.getSituacionId(),
                projection.getDescripcion(),
                projection.getActivo(),
                projection.getEstadoDsc()
        );
    }

    public static SituacionResponse from(SituacionEntity entity) {
        return new SituacionResponse(
                entity.getSituacionId(),
                entity.getDescripcion(),
                entity.getActivo(),
                Boolean.TRUE.equals(entity.getActivo()) ? "Activo" : "Inactivo"
        );
    }
}
