package com.proyecto.integrador.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import org.junit.jupiter.api.Test;
import com.proyecto.integrador.model.request.VariedadRegistroRequest;
import com.proyecto.integrador.repository.*;
import com.proyecto.integrador.service.impl.VariedadServiceImpl;

class VariedadServiceTest {
    @Test
    void listar_noUsaBaseDeDatos() {
        var service = new VariedadServiceImpl(TestMocks.repository(VariedadRepository.class),
                TestMocks.repository(ProductoRepository.class));
        assertDoesNotThrow(() -> service.listarVariedades(null, null, null, TestMocks.page()));
    }

    @Test
    void registrar_usaRepositoriosMock() {
        var service = new VariedadServiceImpl(TestMocks.repository(VariedadRepository.class), TestMocks.repository(ProductoRepository.class));
        assertDoesNotThrow(() -> service.registrar(new VariedadRegistroRequest(0, 1, "Hass")));
    }

    @Test
    void actualizar_delegaEnRegistrarConRepositoriosMock() {
        var service = new VariedadServiceImpl(TestMocks.repository(VariedadRepository.class), TestMocks.repository(ProductoRepository.class));
        assertDoesNotThrow(() -> service.actualizar(new VariedadRegistroRequest(0, 1, "Hass")));
    }

    @Test
    void cambiarEstado_usaRepositorioMock() {
        var service = new VariedadServiceImpl(TestMocks.repository(VariedadRepository.class), TestMocks.repository(ProductoRepository.class));
        assertDoesNotThrow(() -> service.cambiarEstado(1, true));
    }
}
