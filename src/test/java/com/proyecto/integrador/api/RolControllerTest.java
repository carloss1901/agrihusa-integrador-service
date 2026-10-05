package com.proyecto.integrador.api;

import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import com.proyecto.integrador.service.RolService;

class RolControllerTest {
    @Test
    void listar_delegaAlServicio() {
        RolService service = mock(RolService.class);
        new RolController(service).listarRoles("Administrador", true, 1, 10);
        verify(service).listarRoles("Administrador", true, PageRequest.of(0, 10));
    }
}
