package com.proyecto.integrador.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DespachoResumenResponse {

    private Long totalDespachos;
    private List<PorUnidad> porUnidad;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PorUnidad {
        private String unidadMedida;
        private Long totalDespachos;
        private BigDecimal cantidadTotal;
    }
}
