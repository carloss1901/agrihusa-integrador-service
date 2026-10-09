package com.proyecto.integrador.model.response;

import com.proyecto.integrador.model.entity.PuertoLlegadaEntity;
import com.proyecto.integrador.model.projection.PuertoLlegadaProjection;

public record PuertoLlegadaResponse(
        Integer puertoLlegadaId,
        String codigo,
        String puerto,
        String pais,
        Boolean activo,
        String estadoDsc
) {

    public static PuertoLlegadaResponse from(PuertoLlegadaProjection projection) {
        return new PuertoLlegadaResponse(
                projection.getPuertoLlegadaId(),
                projection.getCodigo(),
                projection.getPuerto(),
                projection.getPais(),
                projection.getActivo(),
                projection.getEstadoDsc()
        );
    }

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
