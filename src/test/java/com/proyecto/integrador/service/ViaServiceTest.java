package com.proyecto.integrador.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import org.junit.jupiter.api.Test;
import com.proyecto.integrador.model.request.ViaRegistroRequest;
import com.proyecto.integrador.repository.ViaRepository;
import com.proyecto.integrador.service.impl.ViaServiceImpl;

class ViaServiceTest {
    @Test
    void listar_noUsaBaseDeDatos() {
        var service = new ViaServiceImpl(TestMocks.repository(ViaRepository.class));
        assertDoesNotThrow(() -> service.listarVias(null, null, TestMocks.page()));
    }

    @Test
    void registrar_usaRepositorioMock() {
        var service = new ViaServiceImpl(TestMocks.repository(ViaRepository.class));
        assertDoesNotThrow(() -> service.registrar(new ViaRegistroRequest(0, "Maritima")));
    }

    @Test
    void actualizar_delegaEnRegistrarConRepositorioMock() {
        var service = new ViaServiceImpl(TestMocks.repository(ViaRepository.class));
        assertDoesNotThrow(() -> service.actualizar(new ViaRegistroRequest(0, "Maritima")));
    }

    @Test
    void cambiarEstado_usaRepositorioMock() {
        var service = new ViaServiceImpl(TestMocks.repository(ViaRepository.class));
        assertDoesNotThrow(() -> service.cambiarEstado(1, true));
    }
}
