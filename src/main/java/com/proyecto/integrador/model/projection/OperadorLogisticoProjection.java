package com.proyecto.integrador.model.projection;

import java.time.LocalDate;

public interface OperadorLogisticoProjection {

    Integer getOperadorLogisticoId();

    String getRuc();

    String getRazonSocial();

    String getNombreComercial();

    String getContacto();

    String getCorreo();

    String getTelefono();

    String getDireccion();

    Boolean getActivo();

    String getEstadoDsc();

    LocalDate getFechaCreacion();

    LocalDate getFechaModificacion();
}
