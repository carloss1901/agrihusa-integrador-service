package com.proyecto.integrador.api;

import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import com.proyecto.integrador.service.BitacoraService;

class BitacoraControllerTest {
    @Test
    void listar_delegaAlServicio() {
        BitacoraService service = mock(BitacoraService.class);
        new BitacoraController(service).listarBitacoras(1, "modulo", "accion", "entidad", "resultado", true, 1, 10);
        verify(service).listarBitacoras(1, "modulo", "accion", "entidad", "resultado", true, PageRequest.of(0, 10));
    }
}
