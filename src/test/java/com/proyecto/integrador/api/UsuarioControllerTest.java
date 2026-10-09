package com.proyecto.integrador.api;

import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;
import com.proyecto.integrador.model.request.CambiarContraseniaRequest;
import com.proyecto.integrador.service.UsuarioService;

class UsuarioControllerTest {
    @Test
    void cambiarContrasenia_delegaAlServicio() {
        UsuarioService service = mock(UsuarioService.class);
        CambiarContraseniaRequest request = new CambiarContraseniaRequest("anterior", "nueva123", "nueva123");
        new UsuarioController(service).cambiarContrasenia(request);
        verify(service).cambiarContrasenia(request);
    }
}
