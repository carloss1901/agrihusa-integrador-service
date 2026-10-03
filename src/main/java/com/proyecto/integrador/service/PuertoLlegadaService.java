package com.proyecto.integrador.service;

import com.proyecto.integrador.model.request.PuertoLlegadaRegistroRequest;
import com.proyecto.integrador.model.response.PuertoLlegadaResponse;
import com.proyecto.integrador.utils.CustomPage;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface PuertoLlegadaService {

    CustomPage<PuertoLlegadaResponse> listarPuertos(
            String texto, String pais, Boolean activo, Pageable pageable);

    ResponseEntity<Object> registrar(PuertoLlegadaRegistroRequest request);

    ResponseEntity<Object> cambiarEstado(Integer puertoLlegadaId, Boolean activo);
}
