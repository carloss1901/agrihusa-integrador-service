package com.proyecto.integrador.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import org.junit.jupiter.api.Test;
import com.proyecto.integrador.model.request.OperadorLogisticoRegistroRequest;
import com.proyecto.integrador.repository.OperadorLogisticoRepository;
import com.proyecto.integrador.service.impl.OperadorLogisticoServiceImpl;

class OperadorLogisticoServiceTest {
    @Test
    void listar_noUsaBaseDeDatos() {
        var service = new OperadorLogisticoServiceImpl(TestMocks.repository(OperadorLogisticoRepository.class));
        assertDoesNotThrow(() -> service.listarOperadores(null, null, TestMocks.page()));
    }

    @Test
    void registrar_usaRepositorioMock() {
        var service = new OperadorLogisticoServiceImpl(TestMocks.repository(OperadorLogisticoRepository.class));
        assertDoesNotThrow(() -> service.registrar(new OperadorLogisticoRegistroRequest(0, "20100000001", "Operador", null, null, null, null, null)));
    }

    @Test
    void actualizar_delegaEnRegistrarConRepositorioMock() {
        var service = new OperadorLogisticoServiceImpl(TestMocks.repository(OperadorLogisticoRepository.class));
        assertDoesNotThrow(() -> service.actualizar(new OperadorLogisticoRegistroRequest(0, "20100000001", "Operador", null, null, null, null, null)));
    }

    @Test
    void cambiarEstado_usaRepositorioMock() {
        var service = new OperadorLogisticoServiceImpl(TestMocks.repository(OperadorLogisticoRepository.class));
        assertDoesNotThrow(() -> service.cambiarEstado(1, true));
    }
}
