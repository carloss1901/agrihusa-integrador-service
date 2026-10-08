package com.proyecto.integrador.service;

import com.proyecto.integrador.utils.MessageResponse;

import com.proyecto.integrador.model.request.VariedadRegistroRequest;
import com.proyecto.integrador.model.response.VariedadResponse;
import com.proyecto.integrador.utils.CustomPage;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface VariedadService {

    CustomPage<VariedadResponse> listarVariedades(
            String texto, Integer productoId, Boolean activo, Pageable pageable);

    ResponseEntity<MessageResponse> registrar(VariedadRegistroRequest request);
    default ResponseEntity<MessageResponse> actualizar(VariedadRegistroRequest request) {
        return registrar(request);
    }

    ResponseEntity<MessageResponse> cambiarEstado(Integer variedadId, Boolean activo);
}

