package com.proyecto.integrador.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import org.junit.jupiter.api.Test;
import com.proyecto.integrador.model.request.BitacoraRegistroRequest;
import com.proyecto.integrador.repository.BitacoraRepository;
import com.proyecto.integrador.service.impl.BitacoraServiceImpl;

class BitacoraServiceTest {
    @Test
    void listar_noUsaBaseDeDatos() {
        var service = new BitacoraServiceImpl(TestMocks.repository(BitacoraRepository.class));
        assertDoesNotThrow(() -> service.listarBitacoras(null, null, null, null, null, null, TestMocks.page()));
    }

    @Test
    void registrar_usaRepositorioMock() {
        var service = new BitacoraServiceImpl(TestMocks.repository(BitacoraRepository.class));
        var request = new BitacoraRegistroRequest(1, "modulo", "crear", "Entidad", 1, "detalle", "OK");
        assertDoesNotThrow(() -> service.registrar(request));
    }
}
