package com.proyecto.integrador.model.projection;

public interface ClienteProjection {
    Integer getClienteId(); String getTipoDocumento(); String getNumeroDocumento(); String getRazonSocial();
    String getNombreComercial(); String getContacto(); String getCorreo(); String getTelefono(); String getDireccion();
    String getPais(); Boolean getActivo(); String getEstadoDsc();
}
