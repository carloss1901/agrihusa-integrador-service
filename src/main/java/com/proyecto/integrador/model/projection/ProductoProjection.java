package com.proyecto.integrador.model.projection;

public interface ProductoProjection {

    Integer getProductoId();

    String getCodigo();

    String getNombre();

    String getDescripcion();

    Boolean getActivo();

    String getEstadoDsc();
}
