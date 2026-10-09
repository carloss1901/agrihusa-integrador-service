package com.proyecto.integrador.model.response;

import java.util.List;

public record RolDetalleResponse(
        Integer rolId,
        String nombre,
        String descripcion,
        Boolean esSistema,
        Boolean activo,
        List<RolPermisoResponse> permisos
) {
}