package com.proyecto.integrador.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import org.junit.jupiter.api.Test;
import com.proyecto.integrador.model.request.DespachoRegistroRequest;
import java.math.BigDecimal;
import java.time.LocalDate;
import com.proyecto.integrador.repository.*;
import com.proyecto.integrador.service.impl.DespachoServiceImpl;

class DespachoServiceTest {
    @Test
    void listar_noUsaBaseDeDatos() {
        var service = new DespachoServiceImpl(
                TestMocks.repository(DespachoRepository.class), TestMocks.repository(ClienteRepository.class),
                TestMocks.repository(NavieraRepository.class), TestMocks.repository(DestinoRepository.class),
                TestMocks.repository(OperadorLogisticoRepository.class), TestMocks.repository(PuertoLlegadaRepository.class),
                TestMocks.repository(ProductoRepository.class), TestMocks.repository(VariedadRepository.class),
                TestMocks.repository(ViaRepository.class), TestMocks.repository(SituacionRepository.class));
        assertDoesNotThrow(() -> service.listarDespachos(null, null, null, null, TestMocks.page()));
    }

    @Test
    void registrar_usaRepositoriosMock() {
        var service = crearService();
        var request = new DespachoRegistroRequest(0, LocalDate.now(), LocalDate.now().plusDays(1),
                1, 1, 1, 1, 1, 1, 1, 1, 1, BigDecimal.ONE, "KG", "CONT-001", null);
        assertDoesNotThrow(() -> service.registrar(request));
    }

    @Test
    void actualizar_delegaEnRegistrarConRepositoriosMock() {
        var request = new DespachoRegistroRequest(0, LocalDate.now(), LocalDate.now().plusDays(1),
                1, 1, 1, 1, 1, 1, 1, 1, 1, BigDecimal.ONE, "KG", "CONT-001", null);
        assertDoesNotThrow(() -> crearService().actualizar(request));
    }

    @Test
    void cambiarEstado_usaRepositorioMock() {
        assertDoesNotThrow(() -> crearService().cambiarEstado(1, true));
    }

    private DespachoServiceImpl crearService() {
        return new DespachoServiceImpl(TestMocks.repository(DespachoRepository.class), TestMocks.repository(ClienteRepository.class),
                TestMocks.repository(NavieraRepository.class), TestMocks.repository(DestinoRepository.class),
                TestMocks.repository(OperadorLogisticoRepository.class), TestMocks.repository(PuertoLlegadaRepository.class),
                TestMocks.repository(ProductoRepository.class), TestMocks.repository(VariedadRepository.class),
                TestMocks.repository(ViaRepository.class), TestMocks.repository(SituacionRepository.class));
    }
}
