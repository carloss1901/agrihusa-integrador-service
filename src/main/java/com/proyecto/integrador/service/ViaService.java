package com.proyecto.integrador.service;

import com.proyecto.integrador.model.request.ViaRegistroRequest;
import com.proyecto.integrador.model.response.ViaResponse;
import com.proyecto.integrador.utils.CustomPage;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface ViaService {

    CustomPage<ViaResponse> listarVias(String descripcion, Boolean activo, Pageable pageable);

    ResponseEntity<Object> registrar(ViaRegistroRequest request);

    ResponseEntity<Object> cambiarEstado(Integer viaId, Boolean activo);
}
