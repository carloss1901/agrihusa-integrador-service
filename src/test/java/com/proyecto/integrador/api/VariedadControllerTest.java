package com.proyecto.integrador.api;

import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import com.proyecto.integrador.service.VariedadService;

class VariedadControllerTest {
    @Test
    void listar_delegaAlServicio() {
        VariedadService service = mock(VariedadService.class);
        new VariedadController(service).listarVariedades("Hass", 1, true, 1, 10);
        verify(service).listarVariedades("Hass", 1, true, PageRequest.of(0, 10));
    }
}
