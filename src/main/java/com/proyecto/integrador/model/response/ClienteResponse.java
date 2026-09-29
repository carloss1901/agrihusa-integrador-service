package com.proyecto.integrador.model.response;

import com.proyecto.integrador.model.entity.ClienteEntity;
import com.proyecto.integrador.model.projection.ClienteProjection;

public record ClienteResponse(Integer clienteId, String tipoDocumento, String numeroDocumento, String razonSocial,
                              String nombreComercial, String contacto, String correo, String telefono,
                              String direccion, String pais, Boolean activo, String estadoDsc) {
    public static ClienteResponse from(ClienteProjection p) {
        return new ClienteResponse(p.getClienteId(), p.getTipoDocumento(), p.getNumeroDocumento(), p.getRazonSocial(),
                p.getNombreComercial(), p.getContacto(), p.getCorreo(), p.getTelefono(), p.getDireccion(), p.getPais(), p.getActivo(), p.getEstadoDsc());
    }
    public static ClienteResponse from(ClienteEntity e) {
        return new ClienteResponse(e.getClienteId(), e.getTipoDocumento(), e.getNumeroDocumento(), e.getRazonSocial(),
                e.getNombreComercial(), e.getContacto(), e.getCorreo(), e.getTelefono(), e.getDireccion(), e.getPais(), e.getActivo(),
                Boolean.TRUE.equals(e.getActivo()) ? "Activo" : "Inactivo");
    }
}
