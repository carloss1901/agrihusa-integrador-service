package com.proyecto.integrador.model.response;

import com.proyecto.integrador.model.projection.ReporteDespachoProjection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReporteDespachoResponse {
    private Integer despachoId;
    private String codigo;
    private LocalDate fechaDespacho;
    private LocalDate fechaEstimadaLlegada;
    private String cliente;
    private String naviera;
    private String destino;
    private String operadorLogistico;
    private String puertoLlegada;
    private String producto;
    private String variedad;
    private String via;
    private String situacion;
    private BigDecimal cantidad;
    private String unidadMedida;
    private String numeroContenedor;
    private String observaciones;
    private Boolean activo;

    public static ReporteDespachoResponse from(ReporteDespachoProjection p) {
        return new ReporteDespachoResponse(p.getDespachoId(), p.getCodigo(), p.getFechaDespacho(),
                p.getFechaEstimadaLlegada(), p.getCliente(), p.getNaviera(), p.getDestino(),
                p.getOperadorLogistico(), p.getPuertoLlegada(), p.getProducto(), p.getVariedad(),
                p.getVia(), p.getSituacion(), p.getCantidad(), p.getUnidadMedida(),
                p.getNumeroContenedor(), p.getObservaciones(), p.getActivo());
    }
}
