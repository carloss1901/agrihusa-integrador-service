package com.proyecto.integrador.model.response;

import com.proyecto.integrador.model.entity.PuertoLlegadaEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PuertoLlegadaResponse {

    private Integer puertoLlegadaId;
    private String codigo;
    private String puerto;
    private String pais;
    private Boolean activo;
    private String estadoDsc;

    public static PuertoLlegadaResponse from(PuertoLlegadaEntity entity) {
        return new PuertoLlegadaResponse(
                entity.getPuertoLlegadaId(),
                entity.getCodigo(),
                entity.getPuerto(),
                entity.getPais(),
                entity.getActivo(),
                Boolean.TRUE.equals(entity.getActivo()) ? "Activo" : "Inactivo"
        );
    }
}
