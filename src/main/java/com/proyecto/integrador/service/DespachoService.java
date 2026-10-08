package com.proyecto.integrador.service;

import com.proyecto.integrador.utils.MessageResponse;

import com.proyecto.integrador.model.request.DespachoRegistroRequest;
import com.proyecto.integrador.model.response.DespachoResponse;
import com.proyecto.integrador.utils.CustomPage;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface DespachoService {
    CustomPage<DespachoResponse> listarDespachos(String texto, Integer clienteId, Integer situacionId,
                                                  Boolean activo, Pageable pageable);
    ResponseEntity<MessageResponse> registrar(DespachoRegistroRequest request);
    default ResponseEntity<MessageResponse> actualizar(DespachoRegistroRequest request) {
        return registrar(request);
    }
    ResponseEntity<MessageResponse> cambiarEstado(Integer despachoId, Boolean activo);
}

