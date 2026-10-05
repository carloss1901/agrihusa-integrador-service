package com.proyecto.integrador.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import org.junit.jupiter.api.Test;
import com.proyecto.integrador.model.request.PuertoLlegadaRegistroRequest;
import com.proyecto.integrador.repository.PuertoLlegadaRepository;
import com.proyecto.integrador.service.impl.PuertoLlegadaServiceImpl;

class PuertoLlegadaServiceTest {
    @Test
    void listar_noUsaBaseDeDatos() {
        var service = new PuertoLlegadaServiceImpl(TestMocks.repository(PuertoLlegadaRepository.class));
        assertDoesNotThrow(() -> service.listarPuertos(null, null, null, TestMocks.page()));
    }

    @Test
    void registrar_usaRepositorioMock() {
        var service = new PuertoLlegadaServiceImpl(TestMocks.repository(PuertoLlegadaRepository.class));
        assertDoesNotThrow(() -> service.registrar(new PuertoLlegadaRegistroRequest(0, "PTO-001", "Callao", "Peru")));
    }

    @Test
    void actualizar_delegaEnRegistrarConRepositorioMock() {
        var service = new PuertoLlegadaServiceImpl(TestMocks.repository(PuertoLlegadaRepository.class));
        assertDoesNotThrow(() -> service.actualizar(new PuertoLlegadaRegistroRequest(0, "PTO-001", "Callao", "Peru")));
    }

    @Test
    void cambiarEstado_usaRepositorioMock() {
        var service = new PuertoLlegadaServiceImpl(TestMocks.repository(PuertoLlegadaRepository.class));
        assertDoesNotThrow(() -> service.cambiarEstado(1, true));
    }
}
