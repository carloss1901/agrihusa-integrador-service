package com.proyecto.integrador.service;

import com.proyecto.integrador.model.request.NavieraRegistroRequest;
import com.proyecto.integrador.model.response.NavieraResponse;
import com.proyecto.integrador.utils.CustomPage;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface NavieraService {
    CustomPage<NavieraResponse> listarNavieras(String texto, String pais, Boolean activo, Pageable pageable);
    ResponseEntity<Object> registrar(NavieraRegistroRequest request);
    default ResponseEntity<Object> actualizar(NavieraRegistroRequest request) {
        return registrar(request);
    }
    ResponseEntity<Object> cambiarEstado(Integer navieraId, Boolean activo);
}
