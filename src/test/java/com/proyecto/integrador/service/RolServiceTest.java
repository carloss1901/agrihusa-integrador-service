package com.proyecto.integrador.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import org.junit.jupiter.api.Test;
import com.proyecto.integrador.model.request.RolRegistroRequest;
import java.util.List;
import com.proyecto.integrador.repository.*;
import com.proyecto.integrador.service.impl.RolServiceImpl;

class RolServiceTest {
    @Test
    void listar_noUsaBaseDeDatos() {
        var service = new RolServiceImpl(TestMocks.repository(RolRepository.class),
                TestMocks.repository(ModuloRepository.class), TestMocks.repository(PermisoRepository.class),
                TestMocks.repository(RolPermisoRepository.class));
        assertDoesNotThrow(() -> service.listarRoles(null, null, TestMocks.page()));
    }

    @Test
    void listarRolesActivosCombo_usaRepositorio() {
        var service = new RolServiceImpl(TestMocks.repository(RolRepository.class), TestMocks.repository(ModuloRepository.class),
                TestMocks.repository(PermisoRepository.class), TestMocks.repository(RolPermisoRepository.class));
        assertDoesNotThrow(service::listarRolesActivosCombo);
    }

    @Test
    void registrar_usaRepositoriosMock() {
        var service = new RolServiceImpl(TestMocks.repository(RolRepository.class), TestMocks.repository(ModuloRepository.class),
                TestMocks.repository(PermisoRepository.class), TestMocks.repository(RolPermisoRepository.class));
        assertDoesNotThrow(() -> service.registrar(new RolRegistroRequest(0, "Rol", "Descripcion", List.of())));
    }

    @Test
    void actualizar_delegaEnRegistrarConRepositoriosMock() {
        var service = new RolServiceImpl(TestMocks.repository(RolRepository.class), TestMocks.repository(ModuloRepository.class),
                TestMocks.repository(PermisoRepository.class), TestMocks.repository(RolPermisoRepository.class));
        assertDoesNotThrow(() -> service.actualizar(new RolRegistroRequest(0, "Rol", "Descripcion", List.of())));
    }

    @Test
    void cambiarEstado_usaRepositorioMock() {
        var service = new RolServiceImpl(TestMocks.repository(RolRepository.class), TestMocks.repository(ModuloRepository.class),
                TestMocks.repository(PermisoRepository.class), TestMocks.repository(RolPermisoRepository.class));
        assertDoesNotThrow(() -> service.cambiarEstado(1, true));
    }
}
