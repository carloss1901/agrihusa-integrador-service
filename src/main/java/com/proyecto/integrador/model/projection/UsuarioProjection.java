package com.proyecto.integrador.model.projection;

import java.time.LocalDateTime;

public interface UsuarioProjection {

    Integer getUsuarioId();

    String getUsuario();

    String getNombres();

    String getApellidos();

    String getCorreo();

    Integer getRolId();

    String getRolDescripcion();

    Boolean getEsSistema();

    LocalDateTime getUltimoAcceso();

    Integer getEstadoId();

    String getTipo();

    Boolean getActivo();

    String getEstadoDsc();

}
