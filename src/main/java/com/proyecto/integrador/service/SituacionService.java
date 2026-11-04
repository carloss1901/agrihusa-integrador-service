package com.proyecto.integrador.service;

import com.proyecto.integrador.utils.MessageResponse;

import com.proyecto.integrador.model.request.SituacionRegistroRequest;
import com.proyecto.integrador.model.response.SituacionResponse;
import com.proyecto.integrador.utils.CustomPage;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface SituacionService {

    CustomPage<SituacionResponse> listarSituaciones(
            String descripcion, Boolean activo, Pageable pageable);

    ResponseEntity<MessageResponse> registrar(SituacionRegistroRequest request);
    default ResponseEntity<MessageResponse> actualizar(SituacionRegistroRequest request) {
        return registrar(request);
    }

    ResponseEntity<MessageResponse> cambiarEstado(Integer situacionId, Boolean activo);
}

