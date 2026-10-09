package com.proyecto.integrador.model.response;

import com.proyecto.integrador.model.entity.DestinoEntity;
import com.proyecto.integrador.model.projection.DestinoProjection;

public record DestinoResponse(
        Integer destinoId,
        String pais,
        String ciudad,
        Boolean activo,
        String estadoDsc
) {

    public static DestinoResponse from(DestinoProjection projection) {
        return new DestinoResponse(
                projection.getDestinoId(),
                projection.getPais(),
                projection.getCiudad(),
                projection.getActivo(),
                projection.getEstadoDsc()
        );
    }

    public static DestinoResponse from(DestinoEntity entity) {
        return new DestinoResponse(
                entity.getDestinoId(),
                entity.getPais(),
                entity.getCiudad(),
                entity.getActivo(),
                Boolean.TRUE.equals(entity.getActivo()) ? "Activo" : "Inactivo"
        );
    }
}
