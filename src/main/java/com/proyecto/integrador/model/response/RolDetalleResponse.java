package com.proyecto.integrador.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RolDetalleResponse {

    private String nombre;
    private String descripcion;
    private List<RolPermisoResponse> permisos;
}
