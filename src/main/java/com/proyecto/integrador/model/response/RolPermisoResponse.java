package com.proyecto.integrador.model.response;

import java.util.List;

public record RolPermisoResponse(
        String modulo,
        List<String> acciones
) {
}