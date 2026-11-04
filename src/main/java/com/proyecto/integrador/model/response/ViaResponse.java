package com.proyecto.integrador.model.response;

import com.proyecto.integrador.model.entity.ViaEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ViaResponse {

    private Integer viaId;
    private String descripcion;
    private Boolean activo;
    private String estadoDsc;

    public static ViaResponse from(ViaEntity entity) {
        return new ViaResponse(
                entity.getViaId(),
                entity.getDescripcion(),
                entity.getActivo(),
                Boolean.TRUE.equals(entity.getActivo()) ? "Activo" : "Inactivo"
        );
    }
}
