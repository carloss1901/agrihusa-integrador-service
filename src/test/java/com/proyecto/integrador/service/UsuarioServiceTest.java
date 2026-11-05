package com.proyecto.integrador.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.proyecto.integrador.model.entity.RolEntity;
import com.proyecto.integrador.model.entity.UsuarioEntity;
import com.proyecto.integrador.model.entity.UsuarioRolEntity;
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
        request.setContraseniaTemporal("Temporal123*");
        assertDoesNotThrow(() -> service.registrar(request));
    }

    @Test
    void registrar_rechazaContraseniaTemporalAusente() {
        UsuarioRepository usuarioRepository = mock(UsuarioRepository.class);
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
        var service = new UsuarioServiceImpl(
                usuarioRepository,
                mock(RolRepository.class),
                mock(UsuarioRolRepository.class),
                passwordEncoder
        );
        var request = new UsuarioRegistroRequest(
                "12345678", "Nombres", "Paterno", "Materno",
                "correo@correo.com", "999999999", 2
        );

        assertEquals(
                HttpStatus.BAD_REQUEST,
                service.registrar(request).getStatusCode()
        );
        verify(passwordEncoder, never()).encode(any());
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void registrar_codificaContraseniaTemporal() {
        UsuarioRepository usuarioRepository = mock(UsuarioRepository.class);
        RolRepository rolRepository = mock(RolRepository.class);
        UsuarioRolRepository usuarioRolRepository = mock(UsuarioRolRepository.class);
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
        RolEntity rol = mock(RolEntity.class);
        var service = new UsuarioServiceImpl(
                usuarioRepository,
                rolRepository,
                usuarioRolRepository,
                passwordEncoder
        );
        var request = new UsuarioRegistroRequest(
                "12345678", "Nombres", "Paterno", "Materno",
                "correo@correo.com", "999999999", 2
        );
        request.setContraseniaTemporal("Temporal123*");

        when(rolRepository.findById(2)).thenReturn(Optional.of(rol));
        when(rol.getRolId()).thenReturn(2);
        when(passwordEncoder.encode("Temporal123*")).thenReturn("hash-seguro");
        when(usuarioRepository.save(any(UsuarioEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        assertEquals(
                HttpStatus.CREATED,
                service.registrar(request).getStatusCode()
        );
        verify(passwordEncoder).encode("Temporal123*");
        verify(usuarioRepository).save(
                org.mockito.ArgumentMatchers.argThat(usuario ->
                        "hash-seguro".equals(usuario.getContrasenia())
                                && Boolean.TRUE.equals(usuario.getResetContrasenia())
                )
        );
    }

    @Test
    void actualizar_reutilizaRelacionConElMismoRol() {
        UsuarioRepository usuarioRepository = mock(UsuarioRepository.class);
        RolRepository rolRepository = mock(RolRepository.class);
        UsuarioRolRepository usuarioRolRepository = mock(UsuarioRolRepository.class);
        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setUsuarioId(10);
        usuario.setDni("12345678");
        usuario.setCorreo("correo@correo.com");
        RolEntity rol = mock(RolEntity.class);
        UsuarioRolEntity relacion = new UsuarioRolEntity();
        relacion.setUsuarioRolId(20);
        relacion.setUsuarioId(10);
        relacion.setRolId(2);
        relacion.setActivo(Boolean.TRUE);
        var service = new UsuarioServiceImpl(
                usuarioRepository,
                rolRepository,
                usuarioRolRepository,
                mock(PasswordEncoder.class)
        );
        var request = new UsuarioRegistroRequest(
                "12345678", "Nombres", "Paterno", "Materno",
                "correo@correo.com", "999999999", 2
        );
        request.setUsuarioId(10);

        when(usuarioRepository.findById(10))
                .thenReturn(Optional.of(usuario));
        when(rolRepository.findById(2))
                .thenReturn(Optional.of(rol));
        when(rol.getRolId()).thenReturn(2);
        when(usuarioRolRepository
                .findAllByUsuarioIdAndActivoTrue(10))
                .thenReturn(java.util.List.of(relacion));
        when(usuarioRolRepository
                .findByUsuarioIdAndRolId(10, 2))
                .thenReturn(Optional.of(relacion));

        assertEquals(
                HttpStatus.OK,
                service.actualizar(request).getStatusCode()
        );
        verify(usuarioRolRepository).save(relacion);
        assertEquals(Boolean.TRUE, relacion.getActivo());
        assertEquals(20, relacion.getUsuarioRolId());
    }

    @Test
    void cambiarContrasenia_usaRepositoriosMock() {
        var service = new UsuarioServiceImpl(TestMocks.repository(UsuarioRepository.class),
                TestMocks.repository(RolRepository.class), TestMocks.repository(UsuarioRolRepository.class),
                mock(PasswordEncoder.class));
        assertDoesNotThrow(() -> service.cambiarContrasenia(mock(CambiarContraseniaRequest.class)));
    }
}
