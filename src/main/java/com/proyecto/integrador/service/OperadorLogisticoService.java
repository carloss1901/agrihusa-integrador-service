package com.proyecto.integrador.service;

import com.proyecto.integrador.utils.MessageResponse;

import com.proyecto.integrador.model.request.OperadorLogisticoRegistroRequest;
import com.proyecto.integrador.model.response.OperadorLogisticoResponse;
import com.proyecto.integrador.utils.CustomPage;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface OperadorLogisticoService {

    CustomPage<OperadorLogisticoResponse> listarOperadores(
            String texto, Boolean activo, Pageable pageable);

    ResponseEntity<MessageResponse> registrar(OperadorLogisticoRegistroRequest request);
    default ResponseEntity<MessageResponse> actualizar(OperadorLogisticoRegistroRequest request) {
        return registrar(request);
    }

    ResponseEntity<MessageResponse> cambiarEstado(Integer operadorLogisticoId, Boolean activo);
}

