package com.proyecto.integrador.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import com.proyecto.integrador.model.entity.*;
import com.proyecto.integrador.model.request.*;
import com.proyecto.integrador.repository.*;
import com.proyecto.integrador.service.impl.*;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

/** Verifica explícitamente los caminos alternativos de los servicios. */
class ServiceConditionalBranchesTest {

    @Test
    void via_cubreDuplicadoNoEncontradoYEstado() {
        ViaRepository repository = mock(ViaRepository.class);
        ViaServiceImpl service = new ViaServiceImpl(repository);
        when(repository.existsByDescripcionIgnoreCase("Maritima")).thenReturn(true);
        assertEquals(HttpStatus.CONFLICT, service.registrar(new ViaRegistroRequest(0, "Maritima")).getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, service.registrar(new ViaRegistroRequest(1, "Maritima")).getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, service.cambiarEstado(1, true).getStatusCode());
        ViaEntity entity = mock(ViaEntity.class);
        when(repository.findById(1)).thenReturn(Optional.of(entity));
        when(repository.existsByDescripcionIgnoreCaseAndViaIdNot("Nueva", 1)).thenReturn(false);
        assertEquals(HttpStatus.OK, service.registrar(new ViaRegistroRequest(1, "Nueva")).getStatusCode());
        assertEquals(HttpStatus.OK, service.cambiarEstado(1, false).getStatusCode());
        verify(repository, atLeastOnce()).save(any(ViaEntity.class));
    }

    @Test
    void cliente_cubreDuplicadosNoEncontradoYActualizacion() {
        ClienteRepository repository = mock(ClienteRepository.class);
        ClienteServiceImpl service = new ClienteServiceImpl(repository);
        ClienteRegistroRequest nuevo = new ClienteRegistroRequest(0, "RUC", "123", "Cliente", null, null, null, null, null, "Peru");
        when(repository.existsByNumeroDocumentoIgnoreCase("123")).thenReturn(true);
        assertEquals(HttpStatus.CONFLICT, service.registrar(nuevo).getStatusCode());
        when(repository.existsByNumeroDocumentoIgnoreCase("123")).thenReturn(false);
        when(repository.existsByRazonSocialIgnoreCase("Cliente")).thenReturn(true);
        assertEquals(HttpStatus.CONFLICT, service.registrar(nuevo).getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, service.registrar(new ClienteRegistroRequest(1, "RUC", "123", "Cliente", null, null, null, null, null, "Peru")).getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, service.cambiarEstado(1, true).getStatusCode());
        ClienteEntity entity = mock(ClienteEntity.class);
        when(repository.findById(1)).thenReturn(Optional.of(entity));
        when(repository.existsByNumeroDocumentoIgnoreCaseAndClienteIdNot("123", 1)).thenReturn(false);
        when(repository.existsByRazonSocialIgnoreCaseAndClienteIdNot("Cliente", 1)).thenReturn(false);
        assertEquals(HttpStatus.OK, service.registrar(new ClienteRegistroRequest(1, "RUC", "123", "Cliente", null, null, null, null, null, "Peru")).getStatusCode());
    }

    @Test
    void destino_cubreDuplicadoNoEncontradoYEstadoInactivo() {
        DestinoRepository repository = mock(DestinoRepository.class);
        DestinoServiceImpl service = new DestinoServiceImpl(repository);
        when(repository.existsByPaisIgnoreCaseAndCiudadIgnoreCase("Peru", "Lima")).thenReturn(true);
        assertEquals(HttpStatus.CONFLICT, service.registrar(new DestinoRegistroRequest(0, "Peru", "Lima")).getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, service.registrar(new DestinoRegistroRequest(1, "Peru", "Lima")).getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, service.cambiarEstado(1, true).getStatusCode());
        DestinoEntity entity = mock(DestinoEntity.class);
        when(repository.findById(1)).thenReturn(Optional.of(entity));
        when(repository.existsByPaisIgnoreCaseAndCiudadIgnoreCaseAndDestinoIdNot("Peru", "Lima", 1)).thenReturn(false);
        assertEquals(HttpStatus.OK, service.registrar(new DestinoRegistroRequest(1, "Peru", "Lima")).getStatusCode());
        assertEquals(HttpStatus.OK, service.cambiarEstado(1, false).getStatusCode());
    }

    @Test
    void naviera_cubreDuplicadosNoEncontradoYActualizacion() {
        NavieraRepository repository = mock(NavieraRepository.class);
        NavieraServiceImpl service = new NavieraServiceImpl(repository);
        NavieraRegistroRequest request = new NavieraRegistroRequest(0, "NAV-1", "Naviera", "Peru", null, null, null, null);
        when(repository.existsByCodigoIgnoreCase("NAV-1")).thenReturn(true);
        assertEquals(HttpStatus.CONFLICT, service.registrar(request).getStatusCode());
        when(repository.existsByCodigoIgnoreCase("NAV-1")).thenReturn(false);
        when(repository.existsByNombreIgnoreCase("Naviera")).thenReturn(true);
        assertEquals(HttpStatus.CONFLICT, service.registrar(request).getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, service.registrar(new NavieraRegistroRequest(1, "NAV-1", "Naviera", "Peru", null, null, null, null)).getStatusCode());
        NavieraEntity entity = mock(NavieraEntity.class);
        when(repository.findById(1)).thenReturn(Optional.of(entity));
        when(repository.existsByCodigoIgnoreCaseAndNavieraIdNot("NAV-1", 1)).thenReturn(false);
        when(repository.existsByNombreIgnoreCaseAndNavieraIdNot("Naviera", 1)).thenReturn(false);
        assertEquals(HttpStatus.OK, service.registrar(new NavieraRegistroRequest(1, "NAV-1", "Naviera", "Peru", null, null, null, null)).getStatusCode());
    }

    @Test
    void producto_cubreDuplicadosNoEncontradoYEstado() {
        ProductoRepository repository = mock(ProductoRepository.class);
        ProductoServiceImpl service = new ProductoServiceImpl(repository);
        ProductoRegistroRequest request = new ProductoRegistroRequest(0, "P-1", "Producto", "Descripcion");
        when(repository.existsByCodigoIgnoreCase("P-1")).thenReturn(true);
        assertEquals(HttpStatus.CONFLICT, service.registrar(request).getStatusCode());
        when(repository.existsByCodigoIgnoreCase("P-1")).thenReturn(false);
        when(repository.existsByNombreIgnoreCase("Producto")).thenReturn(true);
        assertEquals(HttpStatus.CONFLICT, service.registrar(request).getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, service.registrar(new ProductoRegistroRequest(1, "P-1", "Producto", "Descripcion")).getStatusCode());
        ProductoEntity entity = mock(ProductoEntity.class);
        when(repository.findById(1)).thenReturn(Optional.of(entity));
        when(repository.existsByCodigoIgnoreCaseAndProductoIdNot("P-1", 1)).thenReturn(false);
        when(repository.existsByNombreIgnoreCaseAndProductoIdNot("Producto", 1)).thenReturn(false);
        assertEquals(HttpStatus.OK, service.registrar(new ProductoRegistroRequest(1, "P-1", "Producto", "Descripcion")).getStatusCode());
    }

    @Test
    void situacion_cubreDuplicadoNoEncontradoYEstado() {
        SituacionRepository repository = mock(SituacionRepository.class);
        SituacionServiceImpl service = new SituacionServiceImpl(repository);
        when(repository.existsByDescripcionIgnoreCase("Activo")).thenReturn(true);
        assertEquals(HttpStatus.CONFLICT, service.registrar(new SituacionRegistroRequest(0, "Activo")).getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, service.registrar(new SituacionRegistroRequest(1, "Activo")).getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, service.cambiarEstado(1, true).getStatusCode());
        SituacionEntity entity = mock(SituacionEntity.class);
        when(repository.findById(1)).thenReturn(Optional.of(entity));
        when(repository.existsByDescripcionIgnoreCaseAndSituacionIdNot("Activo", 1)).thenReturn(false);
        assertEquals(HttpStatus.OK, service.registrar(new SituacionRegistroRequest(1, "Activo")).getStatusCode());
    }

    @Test
    void puertoLlegada_cubreDuplicadosNoEncontradoYEstado() {
        PuertoLlegadaRepository repository = mock(PuertoLlegadaRepository.class);
        PuertoLlegadaServiceImpl service = new PuertoLlegadaServiceImpl(repository);
        PuertoLlegadaRegistroRequest request = new PuertoLlegadaRegistroRequest(0, "P-1", "Callao", "Peru");
        when(repository.existsByCodigoIgnoreCase("P-1")).thenReturn(true);
        assertEquals(HttpStatus.CONFLICT, service.registrar(request).getStatusCode());
        when(repository.existsByCodigoIgnoreCase("P-1")).thenReturn(false);
        when(repository.existsByPuertoIgnoreCaseAndPaisIgnoreCase("Callao", "Peru")).thenReturn(true);
        assertEquals(HttpStatus.CONFLICT, service.registrar(request).getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, service.registrar(new PuertoLlegadaRegistroRequest(1, "P-1", "Callao", "Peru")).getStatusCode());
    }

    @Test
    void operadorLogistico_cubreDuplicadosYNoEncontrado() {
        OperadorLogisticoRepository repository = mock(OperadorLogisticoRepository.class);
        OperadorLogisticoServiceImpl service = new OperadorLogisticoServiceImpl(repository);
        OperadorLogisticoRegistroRequest request = new OperadorLogisticoRegistroRequest(0, "20100000001", "Operador", null, null, null, null, null);
        when(repository.existsByRucIgnoreCase("20100000001")).thenReturn(true);
        assertEquals(HttpStatus.CONFLICT, service.registrar(request).getStatusCode());
        when(repository.existsByRucIgnoreCase("20100000001")).thenReturn(false);
        when(repository.existsByRazonSocialIgnoreCase("Operador")).thenReturn(true);
        assertEquals(HttpStatus.CONFLICT, service.registrar(request).getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, service.registrar(new OperadorLogisticoRegistroRequest(1, "20100000001", "Operador", null, null, null, null, null)).getStatusCode());
    }

    @Test
    void rol_cubreModuloYPermisoInexistentes() {
        RolRepository rol = mock(RolRepository.class);
        ModuloRepository modulo = mock(ModuloRepository.class);
        PermisoRepository permiso = mock(PermisoRepository.class);
        RolServiceImpl service = new RolServiceImpl(rol, modulo, permiso, mock(RolPermisoRepository.class));
        RolRegistroRequest request = new RolRegistroRequest(0, "Rol", "Descripcion",
                java.util.List.of(new RolPermisoRequest("ventas", java.util.List.of("crear"))));
        assertEquals(HttpStatus.BAD_REQUEST, service.registrar(request).getStatusCode());
        ModuloEntity moduloEntity = mock(ModuloEntity.class);
        when(modulo.findByCodigoAndActivoTrue("ventas")).thenReturn(Optional.of(moduloEntity));
        when(permiso.findByAccionAndActivoTrue("crear")).thenReturn(Optional.empty());
        assertEquals(HttpStatus.BAD_REQUEST, service.registrar(request).getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, service.registrar(new RolRegistroRequest(1, "Rol", "Descripcion", java.util.List.of())).getStatusCode());
    }

    @Test
    void variedad_cubreProductoInexistenteYEstadoDeProducto() {
        VariedadRepository variedad = mock(VariedadRepository.class);
        ProductoRepository producto = mock(ProductoRepository.class);
        VariedadServiceImpl service = new VariedadServiceImpl(variedad, producto);
        VariedadRegistroRequest request = new VariedadRegistroRequest(0, 1, "Hass");
        assertEquals(HttpStatus.NOT_FOUND, service.registrar(request).getStatusCode());
        ProductoEntity productoEntity = mock(ProductoEntity.class);
        when(producto.findById(1)).thenReturn(Optional.of(productoEntity));
        when(variedad.existsByProductoIdAndNombreIgnoreCase(1, "Hass")).thenReturn(true);
        assertEquals(HttpStatus.CONFLICT, service.registrar(request).getStatusCode());
        VariedadEntity variedadEntity = mock(VariedadEntity.class);
        when(variedad.findById(1)).thenReturn(Optional.of(variedadEntity));
        when(variedadEntity.getProductoId()).thenReturn(1);
        when(productoEntity.getActivo()).thenReturn(false);
        assertEquals(HttpStatus.BAD_REQUEST, service.cambiarEstado(1, true).getStatusCode());
    }

    @Test
    void despacho_cubreReferenciaInexistenteYCodigoDuplicado() {
        DespachoRepository despacho = mock(DespachoRepository.class);
        ClienteRepository cliente = mock(ClienteRepository.class);
        NavieraRepository naviera = mock(NavieraRepository.class);
        DestinoRepository destino = mock(DestinoRepository.class);
        OperadorLogisticoRepository operador = mock(OperadorLogisticoRepository.class);
        PuertoLlegadaRepository puerto = mock(PuertoLlegadaRepository.class);
        ProductoRepository producto = mock(ProductoRepository.class);
        VariedadRepository variedad = mock(VariedadRepository.class);
        ViaRepository via = mock(ViaRepository.class);
        SituacionRepository situacion = mock(SituacionRepository.class);
        DespachoServiceImpl service = new DespachoServiceImpl(despacho, cliente, naviera, destino, operador, puerto, producto, variedad, via, situacion);
        DespachoRegistroRequest request = despachoRequest(0);
        assertEquals(HttpStatus.NOT_FOUND, service.registrar(request).getStatusCode());
        when(cliente.existsById(1)).thenReturn(true);
        when(naviera.existsById(1)).thenReturn(true);
        when(destino.existsById(1)).thenReturn(true);
        when(operador.existsById(1)).thenReturn(true);
        when(puerto.existsById(1)).thenReturn(true);
        when(producto.existsById(1)).thenReturn(true);
        when(variedad.existsById(1)).thenReturn(true);
        when(via.existsById(1)).thenReturn(true);
        when(situacion.existsById(1)).thenReturn(true);
        when(despacho.existsByCodigoIgnoreCase("D-1")).thenReturn(true);
        assertEquals(HttpStatus.CONFLICT, service.registrar(request).getStatusCode());
    }

    @Test
    void usuario_cubreDniCorreoYRolinexistentes() {
        UsuarioRepository usuario = mock(UsuarioRepository.class);
        RolRepository rol = mock(RolRepository.class);
        UsuarioServiceImpl service = new UsuarioServiceImpl(usuario, rol, mock(UsuarioRolRepository.class), mock(PasswordEncoder.class));
        UsuarioRegistroRequest request = new UsuarioRegistroRequest("123", "N", "P", "M", "c@c.com", null, 1);
        when(usuario.existsByDni("123")).thenReturn(true);
        assertEquals(HttpStatus.CONFLICT, service.registrar(request).getStatusCode());
        when(usuario.existsByDni("123")).thenReturn(false);
        when(usuario.existsByCorreo("c@c.com")).thenReturn(true);
        assertEquals(HttpStatus.CONFLICT, service.registrar(request).getStatusCode());
        when(usuario.existsByCorreo("c@c.com")).thenReturn(false);
        assertEquals(HttpStatus.BAD_REQUEST, service.registrar(request).getStatusCode());
    }

    @Test
    void login_cubreCredencialesInvalidasYLoginSinRoles() {
        UsuarioRepository usuario = mock(UsuarioRepository.class);
        PasswordEncoder encoder = mock(PasswordEncoder.class);
        JwtService jwt = mock(JwtService.class);
        LoginServiceImpl service = new LoginServiceImpl(usuario, mock(UsuarioRolRepository.class), mock(RolRepository.class),
                mock(RolPermisoRepository.class), mock(ModuloRepository.class), mock(PermisoRepository.class), encoder, jwt);
        when(usuario.findByUsuarioAndActivoTrue("u")).thenReturn(Optional.empty());
        assertEquals(HttpStatus.UNAUTHORIZED, service.login(new LoginRequest("u", "p")).getStatusCode());
        UsuarioEntity entity = mock(UsuarioEntity.class);
        when(usuario.findByUsuarioAndActivoTrue("u")).thenReturn(Optional.of(entity));
        when(encoder.matches("p", null)).thenReturn(true);
        when(entity.getUsuarioId()).thenReturn(1);
        when(jwt.generateToken(entity, java.util.List.of())).thenReturn("token");
        assertEquals(HttpStatus.OK, service.login(new LoginRequest("u", "p")).getStatusCode());
    }

    private static DespachoRegistroRequest despachoRequest(Integer id) {
        return new DespachoRegistroRequest(id, "D-1", LocalDate.now(), LocalDate.now().plusDays(1),
                1, 1, 1, 1, 1, 1, 1, 1, 1, BigDecimal.ONE, "KG", "CONT-1", null);
    }
}
