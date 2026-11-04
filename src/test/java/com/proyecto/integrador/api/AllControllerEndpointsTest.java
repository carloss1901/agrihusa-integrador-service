package com.proyecto.integrador.api;

import com.proyecto.integrador.model.request.*;
import com.proyecto.integrador.service.*;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class AllControllerEndpointsTest {

    @Test
    void bitacora_registrar_invocaAlServicio() {
        BitacoraService service = mock(BitacoraService.class);
        BitacoraRegistroRequest request = mock(BitacoraRegistroRequest.class);

        new BitacoraController(service).registrarBitacora(request);

        verify(service).registrar(request);
    }

    @Test
    void cliente_invocaTodosLosEndpointsDeEscritura() {
        ClienteService service = mock(ClienteService.class);
        ClienteRegistroRequest request = mock(ClienteRegistroRequest.class);
        ClienteController controller = new ClienteController(service);

        controller.registrarCliente(request);
        controller.actualizarCliente(request);
        controller.cambiarEstadoCliente(1, true);

        verify(service).registrar(request);
        verify(service).actualizar(request);
        verify(service).cambiarEstado(1, true);
    }

    @Test
    void despacho_invocaTodosLosEndpointsDeEscritura() {
        DespachoService service = mock(DespachoService.class);
        DespachoRegistroRequest request = mock(DespachoRegistroRequest.class);
        DespachoController controller = new DespachoController(service);

        controller.registrarDespacho(request);
        controller.actualizarDespacho(request);
        controller.cambiarEstadoDespacho(1, true);

        verify(service).registrar(request);
        verify(service).actualizar(request);
        verify(service).cambiarEstado(1, true);
    }

    @Test
    void destino_invocaTodosLosEndpointsDeEscritura() {
        DestinoService service = mock(DestinoService.class);
        DestinoRegistroRequest request = mock(DestinoRegistroRequest.class);
        DestinoController controller = new DestinoController(service);

        controller.registrarDestino(request);
        controller.actualizarDestino(request);
        controller.cambiarEstadoDestino(1, true);

        verify(service).registrar(request);
        verify(service).actualizar(request);
        verify(service).cambiarEstado(1, true);
    }

    @Test
    void naviera_invocaTodosLosEndpointsDeEscritura() {
        NavieraService service = mock(NavieraService.class);
        NavieraRegistroRequest request = mock(NavieraRegistroRequest.class);
        NavieraController controller = new NavieraController(service);

        controller.registrarNaviera(request);
        controller.actualizarNaviera(request);
        controller.cambiarEstadoNaviera(1, true);

        verify(service).registrar(request);
        verify(service).actualizar(request);
        verify(service).cambiarEstado(1, true);
    }

    @Test
    void operadorLogistico_invocaTodosLosEndpointsDeEscritura() {
        OperadorLogisticoService service = mock(OperadorLogisticoService.class);
        OperadorLogisticoRegistroRequest request = mock(OperadorLogisticoRegistroRequest.class);
        OperadorLogisticoController controller = new OperadorLogisticoController(service);

        controller.registrarOperadorLogistico(request);
        controller.actualizarOperadorLogistico(request);
        controller.cambiarEstadoOperadorLogistico(1, true);

        verify(service).registrar(request);
        verify(service).actualizar(request);
        verify(service).cambiarEstado(1, true);
    }

    @Test
    void producto_invocaTodosLosEndpointsDeEscritura() {
        ProductoService service = mock(ProductoService.class);
        ProductoRegistroRequest request = mock(ProductoRegistroRequest.class);
        ProductoController controller = new ProductoController(service);

        controller.registrarProducto(request);
        controller.actualizarProducto(request);
        controller.cambiarEstadoProducto(1, true);

        verify(service).registrar(request);
        verify(service).actualizar(request);
        verify(service).cambiarEstado(1, true);
    }

    @Test
    void puertoLlegada_invocaTodosLosEndpointsDeEscritura() {
        PuertoLlegadaService service = mock(PuertoLlegadaService.class);
        PuertoLlegadaRegistroRequest request = mock(PuertoLlegadaRegistroRequest.class);
        PuertoLlegadaController controller = new PuertoLlegadaController(service);

        controller.registrarPuertoLlegada(request);
        controller.actualizarPuertoLlegada(request);
        controller.cambiarEstadoPuertoLlegada(1, true);

        verify(service).registrar(request);
        verify(service).actualizar(request);
        verify(service).cambiarEstado(1, true);
    }

    @Test
    void rol_invocaTodosLosEndpointsDeEscritura() {
        RolService service = mock(RolService.class);
        RolRegistroRequest request = mock(RolRegistroRequest.class);
        RolController controller = new RolController(service);

        controller.registrarRol(request);
        controller.actualizarRol(request);
        controller.cambiarEstadoRol(1, true);

        verify(service).registrar(request);
        verify(service).actualizar(request);
        verify(service).cambiarEstado(1, true);
    }

    @Test
    void situacion_invocaTodosLosEndpointsDeEscritura() {
        SituacionService service = mock(SituacionService.class);
        SituacionRegistroRequest request = mock(SituacionRegistroRequest.class);
        SituacionController controller = new SituacionController(service);

        controller.registrarSituacion(request);
        controller.actualizarSituacion(request);
        controller.cambiarEstadoSituacion(1, true);

        verify(service).registrar(request);
        verify(service).actualizar(request);
        verify(service).cambiarEstado(1, true);
    }

    @Test
    void usuario_registrarInvocaAlServicio() {
        UsuarioService service = mock(UsuarioService.class);
        UsuarioRegistroRequest request = mock(UsuarioRegistroRequest.class);

        new UsuarioController(service).registrarUsuario(request);

        verify(service).registrar(request);
    }

    @Test
    void variedad_invocaTodosLosEndpointsDeEscritura() {
        VariedadService service = mock(VariedadService.class);
        VariedadRegistroRequest request = mock(VariedadRegistroRequest.class);
        VariedadController controller = new VariedadController(service);

        controller.registrarVariedad(request);
        controller.actualizarVariedad(request);
        controller.cambiarEstadoVariedad(1, true);

        verify(service).registrar(request);
        verify(service).actualizar(request);
        verify(service).cambiarEstado(1, true);
    }

    @Test
    void via_invocaTodosLosEndpointsDeEscritura() {
        ViaService service = mock(ViaService.class);
        ViaRegistroRequest request = mock(ViaRegistroRequest.class);
        ViaController controller = new ViaController(service);

        controller.registrarVia(request);
        controller.actualizarVia(request);
        controller.cambiarEstadoVia(1, true);

        verify(service).registrar(request);
        verify(service).actualizar(request);
        verify(service).cambiarEstado(1, true);
    }

    @Test
    void usuario_cambiarContraseniaInvocaAlServicio() {
        UsuarioService service = mock(UsuarioService.class);
        CambiarContraseniaRequest request = mock(CambiarContraseniaRequest.class);

        new UsuarioController(service).cambiarContraseniaUsuario(request);

        verify(service).cambiarContrasenia(request);
    }
}
