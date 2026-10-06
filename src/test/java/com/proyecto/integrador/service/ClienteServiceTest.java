package com.proyecto.integrador.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import org.junit.jupiter.api.Test;
import com.proyecto.integrador.model.request.ClienteRegistroRequest;
import com.proyecto.integrador.repository.ClienteRepository;
import com.proyecto.integrador.service.impl.ClienteServiceImpl;

class ClienteServiceTest {
    @Test
    void listar_noUsaBaseDeDatos() {
        var service = new ClienteServiceImpl(TestMocks.repository(ClienteRepository.class));
        assertDoesNotThrow(() -> service.listarClientes(null, null, null, TestMocks.page()));
    }

    @Test
    void registrar_usaRepositorioMock() {
        var service = new ClienteServiceImpl(TestMocks.repository(ClienteRepository.class));
        var request = new ClienteRegistroRequest(0, "RUC", "123", "Cliente", null, null, null, null, null, "Peru");
        assertDoesNotThrow(() -> service.registrar(request));
    }

    @Test
    void actualizar_delegaEnRegistrarConRepositorioMock() {
        var service = new ClienteServiceImpl(TestMocks.repository(ClienteRepository.class));
        assertDoesNotThrow(() -> service.actualizar(new ClienteRegistroRequest(0, "RUC", "123", "Cliente", null, null, null, null, null, "Peru")));
    }

    @Test
    void cambiarEstado_usaRepositorioMock() {
        var service = new ClienteServiceImpl(TestMocks.repository(ClienteRepository.class));
        assertDoesNotThrow(() -> service.cambiarEstado(1, true));
    }
}
