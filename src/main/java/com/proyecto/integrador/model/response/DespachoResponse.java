package com.proyecto.integrador.model.response;

import com.proyecto.integrador.model.entity.DespachoEntity;
import com.proyecto.integrador.model.projection.DespachoProjection;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DespachoResponse(Integer despachoId, String codigo, LocalDate fechaDespacho,
                               LocalDate fechaEstimadaLlegada, Integer clienteId, Integer navieraId,
                               Integer destinoId, Integer operadorLogisticoId, Integer puertoLlegadaId,
                               Integer productoId, Integer variedadId, Integer viaId, Integer situacionId,
                               BigDecimal cantidad, String unidadMedida, String numeroContenedor,
                               String observaciones, Boolean activo, String estadoDsc) {

    public static DespachoResponse from(DespachoProjection p) {
        return new DespachoResponse(p.getDespachoId(), p.getCodigo(), p.getFechaDespacho(),
                p.getFechaEstimadaLlegada(), p.getClienteId(), p.getNavieraId(), p.getDestinoId(),
                p.getOperadorLogisticoId(), p.getPuertoLlegadaId(), p.getProductoId(), p.getVariedadId(),
                p.getViaId(), p.getSituacionId(), p.getCantidad(), p.getUnidadMedida(),
                p.getNumeroContenedor(), p.getObservaciones(), p.getActivo(), p.getEstadoDsc());
    }

    public static DespachoResponse from(DespachoEntity e) {
        return new DespachoResponse(e.getDespachoId(), e.getCodigo(), e.getFechaDespacho(),
                e.getFechaEstimadaLlegada(), e.getClienteId(), e.getNavieraId(), e.getDestinoId(),
                e.getOperadorLogisticoId(), e.getPuertoLlegadaId(), e.getProductoId(), e.getVariedadId(),
                e.getViaId(), e.getSituacionId(), e.getCantidad(), e.getUnidadMedida(),
                e.getNumeroContenedor(), e.getObservaciones(), e.getActivo(),
                Boolean.TRUE.equals(e.getActivo()) ? "Activo" : "Inactivo");
    }
}
