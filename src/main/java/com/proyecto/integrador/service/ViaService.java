package com.proyecto.integrador.service;

import com.proyecto.integrador.utils.MessageResponse;

import com.proyecto.integrador.model.request.ViaRegistroRequest;
import com.proyecto.integrador.model.response.ViaResponse;
import com.proyecto.integrador.utils.CustomPage;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface ViaService {

    CustomPage<ViaResponse> listarVias(String descripcion, Boolean activo, Pageable pageable);

    ResponseEntity<MessageResponse> registrar(ViaRegistroRequest request);
    default ResponseEntity<MessageResponse> actualizar(ViaRegistroRequest request) {
        return registrar(request);
    }

    ResponseEntity<MessageResponse> cambiarEstado(Integer viaId, Boolean activo);
}

