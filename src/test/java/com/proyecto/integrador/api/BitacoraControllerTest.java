package com.proyecto.integrador.api;

import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import com.proyecto.integrador.service.BitacoraService;

class BitacoraControllerTest {
    @Test
    void listar_delegaAlServicio() {
        BitacoraService service = mock(BitacoraService.class);
        new BitacoraController(service).listar(
                "71477205",
                "modulo",
                "accion",
                "entidad",
                "resultado",
                true,
                null,
                null,
                1,
                10
        );

        verify(service).listarBitacoras(
                "71477205",
                "modulo",
                "accion",
                "entidad",
                "resultado",
                true,
                null,
                null,
                PageRequest.of(0, 10)
        );
    }
}
