package com.proyecto.integrador.model.response;

import com.proyecto.integrador.model.entity.NavieraEntity;
import com.proyecto.integrador.model.projection.NavieraProjection;

public record NavieraResponse(Integer navieraId, String codigo, String nombre, String pais,
                              String contacto, String correo, String telefono, String sitioWeb,
                              Boolean activo, String estadoDsc) {
    public static NavieraResponse from(NavieraProjection p) {
        return new NavieraResponse(p.getNavieraId(), p.getCodigo(), p.getNombre(), p.getPais(),
                p.getContacto(), p.getCorreo(), p.getTelefono(), p.getSitioWeb(), p.getActivo(), p.getEstadoDsc());
    }
    public static NavieraResponse from(NavieraEntity e) {
        return new NavieraResponse(e.getNavieraId(), e.getCodigo(), e.getNombre(), e.getPais(),
                e.getContacto(), e.getCorreo(), e.getTelefono(), e.getSitioWeb(), e.getActivo(),
                Boolean.TRUE.equals(e.getActivo()) ? "Activo" : "Inactivo");
    }
}
