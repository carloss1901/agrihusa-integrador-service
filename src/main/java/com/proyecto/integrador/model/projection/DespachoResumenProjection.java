package com.proyecto.integrador.model.projection;

import java.math.BigDecimal;

public interface DespachoResumenProjection {
    String getUnidadMedida();
    Long getTotalDespachos();
    BigDecimal getCantidadTotal();
}
