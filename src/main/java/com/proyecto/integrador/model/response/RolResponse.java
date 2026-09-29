package com.proyecto.integrador.model.response;

import com.proyecto.integrador.model.entity.RolEntity;
import com.proyecto.integrador.model.projection.RolProjection;

public record RolResponse(
        Integer rolId,
        String nombre,
        String descripcion,
        Boolean activo,
        String estadoDsc
) {

    public static RolResponse from(RolProjection projection) {
        return new RolResponse(
                projection.getRolId(),
                projection.getNombre(),
                projection.getDescripcion(),
                projection.getActivo(),
                projection.getEstadoDsc()
        );
    }

    public static RolResponse from(RolEntity entity) {
        return new RolResponse(
                entity.getRolId(),
                entity.getNombre(),
                entity.getDescripcion(),
                entity.getActivo(),
                Boolean.TRUE.equals(entity.getActivo()) ? "Activo" : "Inactivo"
        );
    }
}
