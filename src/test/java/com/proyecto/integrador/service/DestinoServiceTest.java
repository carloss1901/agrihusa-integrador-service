package com.proyecto.integrador.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import org.junit.jupiter.api.Test;
import com.proyecto.integrador.model.request.DestinoRegistroRequest;
import com.proyecto.integrador.repository.DestinoRepository;
import com.proyecto.integrador.service.impl.DestinoServiceImpl;

class DestinoServiceTest {
    @Test
    void listar_noUsaBaseDeDatos() {
        var service = new DestinoServiceImpl(TestMocks.repository(DestinoRepository.class));
        assertDoesNotThrow(() -> service.listarDestinos(null, null, null, TestMocks.page()));
    }

    @Test
    void registrar_usaRepositorioMock() {
        var service = new DestinoServiceImpl(TestMocks.repository(DestinoRepository.class));
        assertDoesNotThrow(() -> service.registrar(new DestinoRegistroRequest(0, "Peru", "Lima")));
    }

    @Test
    void actualizar_delegaEnRegistrarConRepositorioMock() {
        var service = new DestinoServiceImpl(TestMocks.repository(DestinoRepository.class));
        assertDoesNotThrow(() -> service.actualizar(new DestinoRegistroRequest(0, "Peru", "Lima")));
    }

    @Test
    void cambiarEstado_usaRepositorioMock() {
        var service = new DestinoServiceImpl(TestMocks.repository(DestinoRepository.class));
        assertDoesNotThrow(() -> service.cambiarEstado(1, true));
    }
}
