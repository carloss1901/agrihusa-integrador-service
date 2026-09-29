package com.proyecto.integrador.model.projection;

public interface RolProjection {

    Integer getRolId();

    String getNombre();

    String getDescripcion();

    Boolean getEsSistema();

    Boolean getActivo();

    Long getCantidadPermisos();

    String getEstadoDsc();
}
