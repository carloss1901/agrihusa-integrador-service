package com.proyecto.integrador.model.response;

import com.proyecto.integrador.model.entity.ProductoEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoResponse {

    private Integer productoId;
    private String codigo;
    private String nombre;
    private String descripcion;
    private Boolean activo;
    private String estadoDsc;

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
