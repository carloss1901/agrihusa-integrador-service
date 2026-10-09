package com.proyecto.integrador.model.response;

import com.proyecto.integrador.model.entity.VariedadEntity;
import com.proyecto.integrador.model.projection.VariedadProjection;

public record VariedadResponse(
        Integer variedadId,
        Integer productoId,
        String nombre,
        Boolean activo,
        String estadoDsc
) {

    public static VariedadResponse from(VariedadProjection projection) {
        return new VariedadResponse(
                projection.getVariedadId(),
                projection.getProductoId(),
                projection.getNombre(),
                projection.getActivo(),
                projection.getEstadoDsc()
        );
    }

    public static VariedadResponse from(VariedadEntity entity) {
        return new VariedadResponse(
                entity.getVariedadId(),
                entity.getProductoId(),
                entity.getNombre(),
                entity.getActivo(),
                Boolean.TRUE.equals(entity.getActivo()) ? "Activo" : "Inactivo"
        );
    }
}
