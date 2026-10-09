package com.proyecto.integrador.service;

import com.proyecto.integrador.utils.MessageResponse;

import com.proyecto.integrador.model.request.ProductoRegistroRequest;
import com.proyecto.integrador.model.response.ProductoResponse;
import com.proyecto.integrador.model.response.ComunResponse;
import com.proyecto.integrador.utils.CustomPage;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProductoService {

    CustomPage<ProductoResponse> listarProductos(String texto, Boolean activo, Pageable pageable);

    List<ComunResponse> listarProductosActivosCombo();

    ResponseEntity<MessageResponse> registrar(ProductoRegistroRequest request);
    default ResponseEntity<MessageResponse> actualizar(ProductoRegistroRequest request) {
        return registrar(request);
    }

    ResponseEntity<MessageResponse> cambiarEstado(Integer productoId, Boolean activo);
}

