package com.proyecto.integrador.model.projection;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface DespachoProjection {
    Integer getDespachoId();
    String getCodigo();
    LocalDate getFechaDespacho();
    LocalDate getFechaEstimadaLlegada();
    Integer getClienteId();
    Integer getNavieraId();
    Integer getDestinoId();
    Integer getOperadorLogisticoId();
    Integer getPuertoLlegadaId();
    Integer getProductoId();
    Integer getVariedadId();
    Integer getViaId();
    Integer getSituacionId();
    BigDecimal getCantidad();
    String getUnidadMedida();
    String getNumeroContenedor();
    String getObservaciones();
    Boolean getActivo();
    String getEstadoDsc();
}
