package com.proyecto.integrador.api;

import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import com.proyecto.integrador.service.ClienteService;

class ClienteControllerTest {
    @Test
    void listar_delegaAlServicio() {
        ClienteService service = mock(ClienteService.class);
        new ClienteController(service).listar("texto", "DNI", true, 1, 10);
        verify(service).listarClientes("texto", "DNI", true, PageRequest.of(0, 10));
    }
}
