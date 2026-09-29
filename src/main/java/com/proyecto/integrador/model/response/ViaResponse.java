package com.proyecto.integrador.model.response;

import com.proyecto.integrador.model.entity.ViaEntity;
import com.proyecto.integrador.model.projection.ViaProjection;

public record ViaResponse(
        Integer viaId,
        String descripcion,
        Boolean activo,
        String estadoDsc
) {

    public static ViaResponse from(ViaProjection projection) {
        return new ViaResponse(
                projection.getViaId(),
                projection.getDescripcion(),
                projection.getActivo(),
                projection.getEstadoDsc()
        );
    }

    public static ViaResponse from(ViaEntity entity) {
        return new ViaResponse(
                entity.getViaId(),
                entity.getDescripcion(),
                entity.getActivo(),
                Boolean.TRUE.equals(entity.getActivo()) ? "Activo" : "Inactivo"
        );
    }
}
