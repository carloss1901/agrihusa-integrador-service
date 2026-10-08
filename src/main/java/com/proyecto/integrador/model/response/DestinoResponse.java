package com.proyecto.integrador.model.response;

import com.proyecto.integrador.model.entity.DestinoEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DestinoResponse {

    private Integer destinoId;
    private String pais;
    private String ciudad;
    private Boolean activo;
    private String estadoDsc;

    public static DestinoResponse from(DestinoEntity entity) {
        return new DestinoResponse(
                entity.getDestinoId(),
                entity.getPais(),
                entity.getCiudad(),
                entity.getActivo(),
                Boolean.TRUE.equals(entity.getActivo()) ? "Activo" : "Inactivo"
        );
    }
}
