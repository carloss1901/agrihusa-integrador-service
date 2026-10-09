package com.proyecto.integrador.api;

import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import com.proyecto.integrador.service.NavieraService;

class NavieraControllerTest {
    @Test
    void listar_delegaAlServicio() {
        NavieraService service = mock(NavieraService.class);
        new NavieraController(service).listar("texto", "Perú", true, 1, 10);
        verify(service).listarNavieras("texto", "Perú", true, PageRequest.of(0, 10));
    }
}
