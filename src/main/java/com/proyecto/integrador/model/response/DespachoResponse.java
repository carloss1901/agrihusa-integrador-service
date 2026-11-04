package com.proyecto.integrador.model.response;

import com.proyecto.integrador.model.entity.DespachoEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DespachoResponse {

    private Integer despachoId;
    private String codigo;
    private LocalDate fechaDespacho;
    private LocalDate fechaEstimadaLlegada;
    private Integer clienteId;
    private Integer navieraId;
    private Integer destinoId;
    private Integer operadorLogisticoId;
    private Integer puertoLlegadaId;
    private Integer productoId;
    private Integer variedadId;
    private Integer viaId;
    private Integer situacionId;
    private BigDecimal cantidad;
    private String unidadMedida;
    private String numeroContenedor;
    private String observaciones;
    private Boolean activo;
    private String estadoDsc;

    public static DespachoResponse from(DespachoEntity e) {
        return new DespachoResponse(e.getDespachoId(), e.getCodigo(), e.getFechaDespacho(),
                e.getFechaEstimadaLlegada(), e.getClienteId(), e.getNavieraId(), e.getDestinoId(),
                e.getOperadorLogisticoId(), e.getPuertoLlegadaId(), e.getProductoId(), e.getVariedadId(),
                e.getViaId(), e.getSituacionId(), e.getCantidad(), e.getUnidadMedida(),
                e.getNumeroContenedor(), e.getObservaciones(), e.getActivo(),
                Boolean.TRUE.equals(e.getActivo()) ? "Activo" : "Inactivo");
    }
}
