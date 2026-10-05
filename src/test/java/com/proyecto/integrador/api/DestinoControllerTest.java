package com.proyecto.integrador.api;

import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import com.proyecto.integrador.service.DestinoService;

class DestinoControllerTest {
    @Test
    void listar_delegaAlServicio() {
        DestinoService service = mock(DestinoService.class);
        new DestinoController(service).listarDestinos("Perú", "Lima", true, 1, 10);
        verify(service).listarDestinos("Perú", "Lima", true, PageRequest.of(0, 10));
    }
}
