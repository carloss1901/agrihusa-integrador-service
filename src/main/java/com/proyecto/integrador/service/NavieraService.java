package com.proyecto.integrador.service;

import com.proyecto.integrador.model.request.NavieraRegistroRequest;
import com.proyecto.integrador.model.response.NavieraResponse;
import com.proyecto.integrador.utils.CustomPage;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface NavieraService {
    CustomPage<NavieraResponse> listarNavieras(String texto, String pais, Boolean activo, Pageable pageable);
    List<NavieraResponse> listarActivas();
    ResponseEntity<Object> obtenerPorId(Integer navieraId);
    ResponseEntity<Object> validarDuplicados(String codigo, String nombre, Integer navieraId);
    ResponseEntity<Object> registrar(NavieraRegistroRequest request);
    ResponseEntity<Object> cambiarEstado(Integer navieraId, Boolean activo);
}
