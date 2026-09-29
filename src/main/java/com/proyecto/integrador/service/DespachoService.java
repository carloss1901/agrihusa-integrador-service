package com.proyecto.integrador.service;

import com.proyecto.integrador.model.request.DespachoRegistroRequest;
import com.proyecto.integrador.model.response.DespachoResponse;
import com.proyecto.integrador.utils.CustomPage;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface DespachoService {
    CustomPage<DespachoResponse> listarDespachos(String texto, Integer clienteId, Integer situacionId,
                                                  Boolean activo, Pageable pageable);
    ResponseEntity<Object> registrar(DespachoRegistroRequest request);
    ResponseEntity<Object> cambiarEstado(Integer despachoId, Boolean activo);
}
