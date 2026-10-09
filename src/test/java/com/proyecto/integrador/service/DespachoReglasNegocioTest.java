package com.proyecto.integrador.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.proyecto.integrador.model.entity.DespachoEntity;
import com.proyecto.integrador.model.projection.DespachoResumenProjection;
import com.proyecto.integrador.model.request.DespachoRegistroRequest;
import com.proyecto.integrador.model.response.DespachoResumenResponse;
import com.proyecto.integrador.repository.ClienteRepository;
import com.proyecto.integrador.repository.DespachoRepository;
import com.proyecto.integrador.repository.DestinoRepository;
import com.proyecto.integrador.repository.NavieraRepository;
import com.proyecto.integrador.repository.OperadorLogisticoRepository;
import com.proyecto.integrador.repository.ProductoRepository;
import com.proyecto.integrador.repository.PuertoLlegadaRepository;
import com.proyecto.integrador.repository.SituacionRepository;
import com.proyecto.integrador.repository.VariedadRepository;
import com.proyecto.integrador.repository.ViaRepository;
import com.proyecto.integrador.service.impl.DespachoServiceImpl;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.http.HttpStatus;

class DespachoReglasNegocioTest {

    private DespachoRepository despacho;
    private VariedadRepository variedad;
    private DespachoServiceImpl service;

    @BeforeEach
    void preparar() {
        despacho = mock(DespachoRepository.class);
        variedad = mock(VariedadRepository.class);
        ClienteRepository cliente = mock(ClienteRepository.class);
        NavieraRepository naviera = mock(NavieraRepository.class);
        DestinoRepository destino = mock(DestinoRepository.class);
        OperadorLogisticoRepository operador = mock(OperadorLogisticoRepository.class);
        PuertoLlegadaRepository puerto = mock(PuertoLlegadaRepository.class);
        ProductoRepository producto = mock(ProductoRepository.class);
        ViaRepository via = mock(ViaRepository.class);
        SituacionRepository situacion = mock(SituacionRepository.class);
        when(cliente.existsById(1)).thenReturn(true);
        when(naviera.existsById(1)).thenReturn(true);
        when(destino.existsById(1)).thenReturn(true);
        when(operador.existsById(1)).thenReturn(true);
        when(puerto.existsById(1)).thenReturn(true);
        when(producto.existsById(1)).thenReturn(true);
        when(variedad.existsById(1)).thenReturn(true);
        when(via.existsById(1)).thenReturn(true);
        when(situacion.existsById(1)).thenReturn(true);
        service = new DespachoServiceImpl(despacho, cliente, naviera, destino, operador, puerto,
                producto, variedad, via, situacion);
    }

    private static DespachoRegistroRequest request(LocalDate salida, LocalDate llegada) {
        return new DespachoRegistroRequest(0, salida, llegada,
                1, 1, 1, 1, 1, 1, 1, 1, 1, BigDecimal.TEN, "CAJAS", "CONT-1", null);
    }

    @Test
    void registrar_sinCodigo_generaCodigoCorrelativoAnual() {
        when(variedad.existsByVariedadIdAndProductoId(1, 1)).thenReturn(true);
        when(despacho.obtenerUltimoCorrelativo(anyString())).thenReturn(7);

        var respuesta = service.registrar(request(LocalDate.now(), LocalDate.now().plusDays(10)));

        assertEquals(HttpStatus.CREATED, respuesta.getStatusCode());
        ArgumentCaptor<DespachoEntity> captor = ArgumentCaptor.forClass(DespachoEntity.class);
        verify(despacho).save(captor.capture());
        assertEquals("DES-" + LocalDate.now().getYear() + "-0008", captor.getValue().getCodigo());
        assertEquals(Boolean.TRUE, captor.getValue().getActivo());
    }

    @Test
    void registrar_primerDespachoDelAnio_empiezaEnUno() {
        when(variedad.existsByVariedadIdAndProductoId(1, 1)).thenReturn(true);
        when(despacho.obtenerUltimoCorrelativo(anyString())).thenReturn(null);

        service.registrar(request(LocalDate.now(), LocalDate.now()));

        ArgumentCaptor<DespachoEntity> captor = ArgumentCaptor.forClass(DespachoEntity.class);
        verify(despacho).save(captor.capture());
        assertTrue(captor.getValue().getCodigo().endsWith("-0001"));
    }

    @Test
    void registrar_fechaDeLlegadaAnterior_devuelveBadRequest() {
        when(variedad.existsByVariedadIdAndProductoId(1, 1)).thenReturn(true);

        var respuesta = service.registrar(request(LocalDate.now(), LocalDate.now().minusDays(1)));

        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        verify(despacho, never()).save(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void registrar_variedadDeOtroProducto_devuelveBadRequest() {
        when(variedad.existsByVariedadIdAndProductoId(1, 1)).thenReturn(false);

        var respuesta = service.registrar(request(LocalDate.now(), LocalDate.now().plusDays(1)));

        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        verify(despacho, never()).save(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void resumen_sumaTotalesPorUnidad() {
        DespachoResumenProjection cajas = mock(DespachoResumenProjection.class);
        when(cajas.getUnidadMedida()).thenReturn("CAJAS");
        when(cajas.getTotalDespachos()).thenReturn(2L);
        when(cajas.getCantidadTotal()).thenReturn(new BigDecimal("30.00"));
        DespachoResumenProjection kilos = mock(DespachoResumenProjection.class);
        when(kilos.getUnidadMedida()).thenReturn("KILOGRAMOS");
        when(kilos.getTotalDespachos()).thenReturn(3L);
        when(kilos.getCantidadTotal()).thenReturn(new BigDecimal("500.00"));
        when(despacho.resumenDespachos(null, null, null, null, null, null, null, null))
                .thenReturn(List.of(cajas, kilos));

        DespachoResumenResponse resumen = service.resumenReporte(null, null, null, null, null, null, null, null);

        assertNotNull(resumen);
        assertEquals(5L, resumen.getTotalDespachos());
        assertEquals(2, resumen.getPorUnidad().size());
        assertEquals("CAJAS", resumen.getPorUnidad().get(0).getUnidadMedida());
    }

    @Test
    void registrar_unidadNoPermitida_devuelveBadRequest() {
        when(variedad.existsByVariedadIdAndProductoId(1, 1)).thenReturn(true);
        var req = new DespachoRegistroRequest(0, LocalDate.now(), LocalDate.now().plusDays(1),
                1, 1, 1, 1, 1, 1, 1, 1, 1, BigDecimal.TEN, "LITROS", "CONT-1", null);

        var respuesta = service.registrar(req);

        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
    }
}
