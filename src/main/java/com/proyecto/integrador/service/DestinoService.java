package com.proyecto.integrador.service;

import com.proyecto.integrador.model.request.DestinoRegistroRequest;
import com.proyecto.integrador.model.response.DestinoResponse;
import com.proyecto.integrador.utils.CustomPage;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface DestinoService {

    CustomPage<DestinoResponse> listarDestinos(
            String pais, String ciudad, Boolean activo, Pageable pageable);

    ResponseEntity<Object> registrar(DestinoRegistroRequest request);
    default ResponseEntity<Object> actualizar(DestinoRegistroRequest request) {
        return registrar(request);
    }

    ResponseEntity<Object> cambiarEstado(Integer destinoId, Boolean activo);
}
