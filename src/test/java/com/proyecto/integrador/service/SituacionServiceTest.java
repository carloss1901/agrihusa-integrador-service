package com.proyecto.integrador.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import org.junit.jupiter.api.Test;
import com.proyecto.integrador.model.request.SituacionRegistroRequest;
import com.proyecto.integrador.repository.SituacionRepository;
import com.proyecto.integrador.service.impl.SituacionServiceImpl;

class SituacionServiceTest {
    @Test
    void listar_noUsaBaseDeDatos() {
        var service = new SituacionServiceImpl(TestMocks.repository(SituacionRepository.class));
        assertDoesNotThrow(() -> service.listarSituaciones(null, null, TestMocks.page()));
    }

    @Test
    void registrar_usaRepositorioMock() {
        var service = new SituacionServiceImpl(TestMocks.repository(SituacionRepository.class));
        assertDoesNotThrow(() -> service.registrar(new SituacionRegistroRequest(0, "Activo")));
    }

    @Test
    void actualizar_delegaEnRegistrarConRepositorioMock() {
        var service = new SituacionServiceImpl(TestMocks.repository(SituacionRepository.class));
        assertDoesNotThrow(() -> service.actualizar(new SituacionRegistroRequest(0, "Activo")));
    }

    @Test
    void cambiarEstado_usaRepositorioMock() {
        var service = new SituacionServiceImpl(TestMocks.repository(SituacionRepository.class));
        assertDoesNotThrow(() -> service.cambiarEstado(1, true));
    }
}
