package com.proyecto.integrador.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import org.junit.jupiter.api.Test;
import com.proyecto.integrador.model.request.NavieraRegistroRequest;
import com.proyecto.integrador.repository.NavieraRepository;
import com.proyecto.integrador.service.impl.NavieraServiceImpl;

class NavieraServiceTest {
    @Test
    void listar_noUsaBaseDeDatos() {
        var service = new NavieraServiceImpl(TestMocks.repository(NavieraRepository.class));
        assertDoesNotThrow(() -> service.listarNavieras(null, null, null, TestMocks.page()));
    }

    @Test
    void registrar_usaRepositorioMock() {
        var service = new NavieraServiceImpl(TestMocks.repository(NavieraRepository.class));
        assertDoesNotThrow(() -> service.registrar(new NavieraRegistroRequest(0, "NAV-001", "Naviera", "Peru", null, null, null, null)));
    }

    @Test
    void actualizar_delegaEnRegistrarConRepositorioMock() {
        var service = new NavieraServiceImpl(TestMocks.repository(NavieraRepository.class));
        assertDoesNotThrow(() -> service.actualizar(new NavieraRegistroRequest(0, "NAV-001", "Naviera", "Peru", null, null, null, null)));
    }

    @Test
    void cambiarEstado_usaRepositorioMock() {
        var service = new NavieraServiceImpl(TestMocks.repository(NavieraRepository.class));
        assertDoesNotThrow(() -> service.cambiarEstado(1, true));
    }
}
