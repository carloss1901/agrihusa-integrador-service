package com.proyecto.integrador.service;

import com.proyecto.integrador.model.request.*;
import com.proyecto.integrador.repository.*;
import com.proyecto.integrador.service.impl.*;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class AllServiceMethodsTest {

    @Test
    void bitacora_invocaTodosLosMetodos() {
        var service = new BitacoraServiceImpl(TestMocks.repository(BitacoraRepository.class));
        var request = new BitacoraRegistroRequest(1, "modulo", "crear", "Entidad", 1, "detalle", "OK");

        assertDoesNotThrow(() -> {
            service.listarBitacoras(null, null, null, null, null, null, TestMocks.page());
            service.registrar(request);
        });
    }

    @Test
    void cliente_invocaTodosLosMetodos() {
        var service = new ClienteServiceImpl(TestMocks.repository(ClienteRepository.class));
        var request = new ClienteRegistroRequest(1, "RUC", "123", "Cliente", null, null, null, null, null, "Perú");

        assertDoesNotThrow(() -> {
            service.listarClientes(null, null, null, TestMocks.page());
            service.registrar(request);
            service.cambiarEstado(1, true);
        });
    }

    @Test
    void despacho_invocaTodosLosMetodos() {
        var service = new DespachoServiceImpl(
                TestMocks.repository(DespachoRepository.class), TestMocks.repository(ClienteRepository.class),
                TestMocks.repository(NavieraRepository.class), TestMocks.repository(DestinoRepository.class),
                TestMocks.repository(OperadorLogisticoRepository.class), TestMocks.repository(PuertoLlegadaRepository.class),
                TestMocks.repository(ProductoRepository.class), TestMocks.repository(VariedadRepository.class),
                TestMocks.repository(ViaRepository.class), TestMocks.repository(SituacionRepository.class));
        var request = new DespachoRegistroRequest(1, LocalDate.now(), LocalDate.now().plusDays(1),
                1, 1, 1, 1, 1, 1, 1, 1, 1, BigDecimal.ONE, "KG", "CONT-001", null);

        assertDoesNotThrow(() -> {
            service.listarDespachos(null, null, null, null, TestMocks.page());
            service.registrar(request);
            service.cambiarEstado(1, true);
        });
    }

    @Test
    void destino_invocaTodosLosMetodos() {
        var service = new DestinoServiceImpl(TestMocks.repository(DestinoRepository.class));
        var request = new DestinoRegistroRequest(1, "Perú", "Lima");

        assertDoesNotThrow(() -> {
            service.listarDestinos(null, null, null, TestMocks.page());
            service.registrar(request);
            service.cambiarEstado(1, true);
        });
    }

    @Test
    void naviera_invocaTodosLosMetodos() {
        var service = new NavieraServiceImpl(TestMocks.repository(NavieraRepository.class));
        var request = new NavieraRegistroRequest(1, "NAV-001", "Naviera", "Perú", null, null, null, null);

        assertDoesNotThrow(() -> {
            service.listarNavieras(null, null, null, TestMocks.page());
            service.registrar(request);
            service.cambiarEstado(1, true);
        });
    }

    @Test
    void operadorLogistico_invocaTodosLosMetodos() {
        var service = new OperadorLogisticoServiceImpl(TestMocks.repository(OperadorLogisticoRepository.class));
        var request = new OperadorLogisticoRegistroRequest(1, "20100000001", "Operador", null, null, null, null, null);

        assertDoesNotThrow(() -> {
            service.listarOperadores(null, null, TestMocks.page());
            service.registrar(request);
            service.cambiarEstado(1, true);
        });
    }

    @Test
    void producto_invocaTodosLosMetodos() {
        var service = new ProductoServiceImpl(TestMocks.repository(ProductoRepository.class));
        var request = new ProductoRegistroRequest(1, "PROD-001", "Producto", "Descripción");

        assertDoesNotThrow(() -> {
            service.listarProductos(null, null, TestMocks.page());
            service.registrar(request);
            service.cambiarEstado(1, true);
        });
    }

    @Test
    void puertoLlegada_invocaTodosLosMetodos() {
        var service = new PuertoLlegadaServiceImpl(TestMocks.repository(PuertoLlegadaRepository.class));
        var request = new PuertoLlegadaRegistroRequest(1, "PTO-001", "Callao", "Perú");

        assertDoesNotThrow(() -> {
            service.listarPuertos(null, null, null, TestMocks.page());
            service.registrar(request);
            service.cambiarEstado(1, true);
        });
    }

    @Test
    void rol_invocaTodosLosMetodos() {
        var service = new RolServiceImpl(TestMocks.repository(RolRepository.class),
                TestMocks.repository(ModuloRepository.class), TestMocks.repository(PermisoRepository.class),
                TestMocks.repository(RolPermisoRepository.class));
        var request = new RolRegistroRequest(1, "Rol", "Descripción", List.of());

        assertDoesNotThrow(() -> {
            service.listarRoles(null, null, TestMocks.page());
            service.registrar(request);
            service.cambiarEstado(1, true);
        });
    }

    @Test
    void situacion_invocaTodosLosMetodos() {
        var service = new SituacionServiceImpl(TestMocks.repository(SituacionRepository.class));
        var request = new SituacionRegistroRequest(1, "Activo");

        assertDoesNotThrow(() -> {
            service.listarSituaciones(null, null, TestMocks.page());
            service.registrar(request);
            service.actualizar(request);
            service.cambiarEstado(1, true);
        });
    }

    @Test
    void usuario_invocaTodosLosMetodos() {
        var service = new UsuarioServiceImpl(TestMocks.repository(UsuarioRepository.class),
                TestMocks.repository(RolRepository.class), TestMocks.repository(UsuarioRolRepository.class),
                mock(PasswordEncoder.class));
        var registro = new UsuarioRegistroRequest("12345678", "Nombres", "Paterno", "Materno",
                "correo@correo.com", "999999999", 1);
        var cambio = mock(CambiarContraseniaRequest.class);

        assertDoesNotThrow(() -> {
            service.registrar(registro);
            service.cambiarContrasenia(cambio);
        });
    }

    @Test
    void variedad_invocaTodosLosMetodos() {
        var service = new VariedadServiceImpl(TestMocks.repository(VariedadRepository.class),
                TestMocks.repository(ProductoRepository.class));
        var request = new VariedadRegistroRequest(1, 1, "Hass");

        assertDoesNotThrow(() -> {
            service.listarVariedades(null, null, null, TestMocks.page());
            service.registrar(request);
            service.actualizar(request);
            service.cambiarEstado(1, true);
        });
    }

    @Test
    void via_invocaTodosLosMetodos() {
        var service = new ViaServiceImpl(TestMocks.repository(ViaRepository.class));
        var request = new ViaRegistroRequest(1, "Marítima");

        assertDoesNotThrow(() -> {
            service.listarVias(null, null, TestMocks.page());
            service.registrar(request);
            service.actualizar(request);
            service.cambiarEstado(1, true);
        });
    }
}
