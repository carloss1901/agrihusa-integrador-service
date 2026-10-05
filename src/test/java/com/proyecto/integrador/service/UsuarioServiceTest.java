package com.proyecto.integrador.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.proyecto.integrador.model.request.UsuarioRegistroRequest;
import com.proyecto.integrador.model.request.CambiarContraseniaRequest;
import com.proyecto.integrador.repository.*;
import com.proyecto.integrador.service.impl.UsuarioServiceImpl;

class UsuarioServiceTest {
    @Test
    void registrar_validaRolSinBaseDeDatos() {
        var service = new UsuarioServiceImpl(TestMocks.repository(UsuarioRepository.class),
                TestMocks.repository(RolRepository.class), TestMocks.repository(UsuarioRolRepository.class),
                mock(PasswordEncoder.class));
        var request = new UsuarioRegistroRequest("12345678", "Nombres", "Paterno", "Materno",
                "correo@correo.com", "999999999", 1);
        assertDoesNotThrow(() -> service.registrar(request));
    }

    @Test
    void cambiarContrasenia_usaRepositoriosMock() {
        var service = new UsuarioServiceImpl(TestMocks.repository(UsuarioRepository.class),
                TestMocks.repository(RolRepository.class), TestMocks.repository(UsuarioRolRepository.class),
                mock(PasswordEncoder.class));
        assertDoesNotThrow(() -> service.cambiarContrasenia(mock(CambiarContraseniaRequest.class)));
    }
}
