package com.proyecto.integrador.service;

import com.proyecto.integrador.utils.MessageResponse;

import com.proyecto.integrador.model.entity.RolEntity;
import com.proyecto.integrador.model.request.RolRegistroRequest;
import com.proyecto.integrador.model.response.RolResponse;
import com.proyecto.integrador.utils.CustomPage;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface RolService {

    ResponseEntity<MessageResponse> registrar(RolRegistroRequest request);
    default ResponseEntity<MessageResponse> actualizar(RolRegistroRequest request) {
        return registrar(request);
    }

    ResponseEntity<MessageResponse> cambiarEstado(Integer rolId, Boolean activo);

    CustomPage<RolResponse> listarRoles(String nombre, Boolean activo, Pageable pageable);
}

