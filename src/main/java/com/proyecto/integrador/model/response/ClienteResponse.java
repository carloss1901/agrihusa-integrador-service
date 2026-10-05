package com.proyecto.integrador.model.response;

import com.proyecto.integrador.model.entity.ClienteEntity;
import com.proyecto.integrador.model.projection.ClienteProjection;

import java.time.LocalDate;

public record ClienteResponse(Integer clienteId, String tipoDocumento, String numeroDocumento, String razonSocial,
                              String nombreComercial, String contacto, String correo, String telefono,
                              String direccion, String pais, Boolean activo, String estadoDsc,
                              LocalDate fechaCreacion, LocalDate fechaModificacion) {
    public static ClienteResponse from(ClienteProjection p) {
        return new ClienteResponse(p.getClienteId(), p.getTipoDocumento(), p.getNumeroDocumento(), p.getRazonSocial(),
                p.getNombreComercial(), p.getContacto(), p.getCorreo(), p.getTelefono(), p.getDireccion(), p.getPais(), p.getActivo(), p.getEstadoDsc(),
                p.getFechaCreacion(), p.getFechaModificacion());
    }
    public static ClienteResponse from(ClienteEntity e) {
        return new ClienteResponse(e.getClienteId(), e.getTipoDocumento(), e.getNumeroDocumento(), e.getRazonSocial(),
                e.getNombreComercial(), e.getContacto(), e.getCorreo(), e.getTelefono(), e.getDireccion(), e.getPais(), e.getActivo(),
                Boolean.TRUE.equals(e.getActivo()) ? "Activo" : "Inactivo", e.getFechaCreacion(), e.getFechaModificacion());
    }
}
