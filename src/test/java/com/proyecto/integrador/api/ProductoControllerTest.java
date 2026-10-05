package com.proyecto.integrador.api;

import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import com.proyecto.integrador.service.ProductoService;

class ProductoControllerTest {
    @Test
    void listar_delegaAlServicio() {
        ProductoService service = mock(ProductoService.class);
        new ProductoController(service).listarProductos("texto", true, 1, 10);
        verify(service).listarProductos("texto", true, PageRequest.of(0, 10));
    }
}
