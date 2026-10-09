package com.proyecto.integrador.model.response;

import com.proyecto.integrador.model.entity.RolEntity;
import com.proyecto.integrador.model.projection.RolProjection;

public record RolResponse(
        Integer rolId,
        String nombre,
        String descripcion,
        Boolean esSistema,
        Boolean activo,
        Long cantidadPermisos,
        String estadoDsc
) {

    public static RolResponse from(RolProjection projection) {
        return new RolResponse(
                projection.getRolId(),
                projection.getNombre(),
                projection.getDescripcion(),
                projection.getEsSistema(),
                projection.getActivo(),
                projection.getCantidadPermisos(),
                projection.getEstadoDsc()
        );
    }

    public static RolResponse from(RolEntity entity) {
        return new RolResponse(
                entity.getRolId(),
                entity.getNombre(),
                entity.getDescripcion(),
                entity.getEsSistema(),
                entity.getActivo(),
                0L,
                Boolean.TRUE.equals(entity.getActivo()) ? "Activo" : "Inactivo"
        );
    }
}
