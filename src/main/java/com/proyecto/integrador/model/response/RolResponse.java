package com.proyecto.integrador.model.response;

import com.proyecto.integrador.model.entity.RolEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RolResponse {

    private Integer rolId;
    private String nombre;
    private String descripcion;
    private Boolean esSistema;
    private Boolean activo;
    private Long cantidadPermisos;
    private String estadoDsc;

    public static RolResponse from(RolEntity entity) {
        return new RolResponse(
                entity.getRolId(),
                entity.getNombre(),
                entity.getDescripcion(),
                entity.getEsSistema(),
                entity.getActivo(),
                0L,
                Boolean.TRUE.equals(entity.getActivo()) ? "Activo" : "Inactivo"
        );
    }
}
