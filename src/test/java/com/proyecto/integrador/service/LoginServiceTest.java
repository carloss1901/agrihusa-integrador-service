package com.proyecto.integrador.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.proyecto.integrador.model.request.LoginRequest;
import com.proyecto.integrador.repository.*;
import com.proyecto.integrador.service.impl.LoginServiceImpl;

class LoginServiceTest {
    @Test
    void login_rechazaCredencialesSinBaseDeDatos() {
        var service = new LoginServiceImpl(
                TestMocks.repository(UsuarioRepository.class), TestMocks.repository(UsuarioRolRepository.class),
                TestMocks.repository(RolRepository.class), TestMocks.repository(RolPermisoRepository.class),
                TestMocks.repository(ModuloRepository.class), TestMocks.repository(PermisoRepository.class),
                mock(PasswordEncoder.class), mock(JwtService.class));
        assertDoesNotThrow(() -> service.login(new LoginRequest("usuario", "password")));
    }
}
