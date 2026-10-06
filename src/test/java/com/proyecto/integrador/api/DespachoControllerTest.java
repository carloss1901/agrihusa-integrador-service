package com.proyecto.integrador.api;

import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import com.proyecto.integrador.service.DespachoService;

class DespachoControllerTest {
    @Test
    void listar_delegaAlServicio() {
        DespachoService service = mock(DespachoService.class);
        new DespachoController(service).listar("texto", 1, 2, true, 1, 10);
        verify(service).listarDespachos("texto", 1, 2, true, PageRequest.of(0, 10));
    }
}
