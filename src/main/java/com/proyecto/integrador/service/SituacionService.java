package com.proyecto.integrador.service;

import com.proyecto.integrador.model.request.SituacionRegistroRequest;
import com.proyecto.integrador.model.response.SituacionResponse;
import com.proyecto.integrador.utils.CustomPage;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface SituacionService {

    CustomPage<SituacionResponse> listarSituaciones(
            String descripcion, Boolean activo, Pageable pageable);

    ResponseEntity<Object> registrar(SituacionRegistroRequest request);
    default ResponseEntity<Object> actualizar(SituacionRegistroRequest request) {
        return registrar(request);
    }

    ResponseEntity<Object> cambiarEstado(Integer situacionId, Boolean activo);
}
