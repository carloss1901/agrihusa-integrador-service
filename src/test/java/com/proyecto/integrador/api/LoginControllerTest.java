package com.proyecto.integrador.api;

import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;
import com.proyecto.integrador.model.request.LoginRequest;
import com.proyecto.integrador.service.LoginService;

class LoginControllerTest {
    @Test
    void login_delegaAlServicio() {
        LoginService service = mock(LoginService.class);
        LoginRequest request = new LoginRequest("usuario", "password");
        new LoginController(service).login(request);
        verify(service).login(request);
    }
}
