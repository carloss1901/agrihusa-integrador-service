package com.proyecto.integrador.service;

import com.proyecto.integrador.model.request.VariedadRegistroRequest;
import com.proyecto.integrador.model.response.VariedadResponse;
import com.proyecto.integrador.utils.CustomPage;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface VariedadService {

    CustomPage<VariedadResponse> listarVariedades(
            String texto, Integer productoId, Boolean activo, Pageable pageable);

    ResponseEntity<Object> registrar(VariedadRegistroRequest request);
    default ResponseEntity<Object> actualizar(VariedadRegistroRequest request) {
        return registrar(request);
    }

    ResponseEntity<Object> cambiarEstado(Integer variedadId, Boolean activo);
}
