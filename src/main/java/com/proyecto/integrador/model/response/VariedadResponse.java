package com.proyecto.integrador.model.response;

import com.proyecto.integrador.model.entity.VariedadEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VariedadResponse {

    private Integer variedadId;
    private Integer productoId;
    private String nombre;
    private Boolean activo;
    private String estadoDsc;

    public static VariedadResponse from(VariedadEntity entity) {
        return new VariedadResponse(
                entity.getVariedadId(),
                entity.getProductoId(),
                entity.getNombre(),
                entity.getActivo(),
                Boolean.TRUE.equals(entity.getActivo()) ? "Activo" : "Inactivo"
        );
    }
}
