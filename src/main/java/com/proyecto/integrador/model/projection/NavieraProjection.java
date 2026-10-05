package com.proyecto.integrador.model.projection;

import java.time.LocalDate;

public interface NavieraProjection {
    Integer getNavieraId(); String getCodigo(); String getNombre(); String getPais();
    String getContacto(); String getCorreo(); String getTelefono(); String getSitioWeb();
    Boolean getActivo(); String getEstadoDsc(); LocalDate getFechaCreacion(); LocalDate getFechaModificacion();
}
