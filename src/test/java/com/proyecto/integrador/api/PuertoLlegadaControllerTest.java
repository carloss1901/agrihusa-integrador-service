package com.proyecto.integrador.api;

import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import com.proyecto.integrador.service.PuertoLlegadaService;

class PuertoLlegadaControllerTest {
    @Test
    void listar_delegaAlServicio() {
        PuertoLlegadaService service = mock(PuertoLlegadaService.class);
        new PuertoLlegadaController(service).listarPuertos("texto", "Perú", true, 1, 10);
        verify(service).listarPuertos("texto", "Perú", true, PageRequest.of(0, 10));
    }
}
