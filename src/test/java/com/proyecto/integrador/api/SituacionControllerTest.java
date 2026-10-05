package com.proyecto.integrador.api;

import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import com.proyecto.integrador.service.SituacionService;

class SituacionControllerTest {
    @Test
    void listar_delegaAlServicio() {
        SituacionService service = mock(SituacionService.class);
        new SituacionController(service).listarSituaciones("Activo", true, 1, 10);
        verify(service).listarSituaciones("Activo", true, PageRequest.of(0, 10));
    }
}
