package com.proyecto.integrador.model.projection;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface ReporteDespachoProjection {
    Integer getDespachoId();
    String getCodigo();
    LocalDate getFechaDespacho();
    LocalDate getFechaEstimadaLlegada();
    String getCliente();
    String getNaviera();
    String getDestino();
    String getOperadorLogistico();
    String getPuertoLlegada();
    String getProducto();
    String getVariedad();
    String getVia();
    String getSituacion();
    BigDecimal getCantidad();
    String getUnidadMedida();
    String getNumeroContenedor();
    String getObservaciones();
    Boolean getActivo();
}
