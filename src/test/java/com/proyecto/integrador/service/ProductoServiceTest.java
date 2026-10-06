package com.proyecto.integrador.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import org.junit.jupiter.api.Test;
import com.proyecto.integrador.model.request.ProductoRegistroRequest;
import com.proyecto.integrador.repository.ProductoRepository;
import com.proyecto.integrador.service.impl.ProductoServiceImpl;

class ProductoServiceTest {
    @Test
    void listar_noUsaBaseDeDatos() {
        var service = new ProductoServiceImpl(TestMocks.repository(ProductoRepository.class));
        assertDoesNotThrow(() -> service.listarProductos(null, null, TestMocks.page()));
    }

    @Test
    void registrar_usaRepositorioMock() {
        var service = new ProductoServiceImpl(TestMocks.repository(ProductoRepository.class));
        assertDoesNotThrow(() -> service.registrar(new ProductoRegistroRequest(0, "PROD-001", "Producto", "Descripcion")));
    }

    @Test
    void actualizar_delegaEnRegistrarConRepositorioMock() {
        var service = new ProductoServiceImpl(TestMocks.repository(ProductoRepository.class));
        assertDoesNotThrow(() -> service.actualizar(new ProductoRegistroRequest(0, "PROD-001", "Producto", "Descripcion")));
    }

    @Test
    void cambiarEstado_usaRepositorioMock() {
        var service = new ProductoServiceImpl(TestMocks.repository(ProductoRepository.class));
        assertDoesNotThrow(() -> service.cambiarEstado(1, true));
    }
}
