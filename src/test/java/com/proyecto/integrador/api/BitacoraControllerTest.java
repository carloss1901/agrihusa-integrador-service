package com.proyecto.integrador.api;

import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import com.proyecto.integrador.service.BitacoraService;

class BitacoraControllerTest {
    @Test
    void listar_delegaAlServicio() {
        BitacoraService service = mock(BitacoraService.class);
        new BitacoraController(service).listarBitacoras("usuario", "modulo", "accion", "entidad", "resultado", true,
                null, null, 1, 10);
        verify(service).listarBitacoras("usuario", "modulo", "accion", "entidad", "resultado", true,
                null, null, PageRequest.of(0, 10));
    }
}
