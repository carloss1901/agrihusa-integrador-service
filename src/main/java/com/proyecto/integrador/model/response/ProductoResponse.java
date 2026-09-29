package com.proyecto.integrador.model.response;

import com.proyecto.integrador.model.entity.ProductoEntity;
import com.proyecto.integrador.model.projection.ProductoProjection;

public record ProductoResponse(
        Integer productoId,
        String codigo,
        String nombre,
        String descripcion,
        Boolean activo,
        String estadoDsc
) {

    public static ProductoResponse from(ProductoProjection projection) {
        return new ProductoResponse(
                projection.getProductoId(),
                projection.getCodigo(),
                projection.getNombre(),
                projection.getDescripcion(),
                projection.getActivo(),
                projection.getEstadoDsc()
        );
    }

    public static ProductoResponse from(ProductoEntity entity) {
        return new ProductoResponse(
                entity.getProductoId(),
                entity.getCodigo(),
                entity.getNombre(),
                entity.getDescripcion(),
                entity.getActivo(),
                Boolean.TRUE.equals(entity.getActivo()) ? "Activo" : "Inactivo"
        );
    }
}
