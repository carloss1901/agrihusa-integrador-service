package com.proyecto.integrador.service;

import com.proyecto.integrador.model.request.ProductoRegistroRequest;
import com.proyecto.integrador.model.response.ProductoResponse;
import com.proyecto.integrador.utils.CustomPage;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface ProductoService {

    CustomPage<ProductoResponse> listarProductos(String texto, Boolean activo, Pageable pageable);

    ResponseEntity<Object> registrar(ProductoRegistroRequest request);
    default ResponseEntity<Object> actualizar(ProductoRegistroRequest request) {
        return registrar(request);
    }

    ResponseEntity<Object> cambiarEstado(Integer productoId, Boolean activo);
}
