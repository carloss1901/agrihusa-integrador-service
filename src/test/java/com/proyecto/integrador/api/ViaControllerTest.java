package com.proyecto.integrador.api;

import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import com.proyecto.integrador.service.ViaService;

class ViaControllerTest {
    @Test
    void listar_delegaAlServicio() {
        ViaService service = mock(ViaService.class);
        new ViaController(service).listarVias("Marítima", true, 1, 10);
        verify(service).listarVias("Marítima", true, PageRequest.of(0, 10));
    }
}
