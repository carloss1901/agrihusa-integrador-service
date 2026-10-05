package com.proyecto.integrador.api;

import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import com.proyecto.integrador.service.OperadorLogisticoService;

class OperadorLogisticoControllerTest {
    @Test
    void listar_delegaAlServicio() {
        OperadorLogisticoService service = mock(OperadorLogisticoService.class);
        new OperadorLogisticoController(service).listarOperadores("texto", true, 1, 10);
        verify(service).listarOperadores("texto", true, PageRequest.of(0, 10));
    }
}
