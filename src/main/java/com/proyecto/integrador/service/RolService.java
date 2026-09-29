package com.proyecto.integrador.service;

import com.proyecto.integrador.model.entity.RolEntity;
import com.proyecto.integrador.model.request.RolRegistroRequest;
import com.proyecto.integrador.model.response.RolResponse;
import com.proyecto.integrador.utils.CustomPage;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface RolService {

    ResponseEntity<Object> registrar(RolRegistroRequest request);

    ResponseEntity<Object> cambiarEstado(Integer rolId, Boolean activo);

    CustomPage<RolResponse> listarRoles(String nombre, Boolean activo, Pageable pageable);
}
