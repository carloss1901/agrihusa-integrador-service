package com.proyecto.integrador.service;

import com.proyecto.integrador.model.request.OperadorLogisticoRegistroRequest;
import com.proyecto.integrador.model.response.OperadorLogisticoResponse;
import com.proyecto.integrador.utils.CustomPage;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface OperadorLogisticoService {

    CustomPage<OperadorLogisticoResponse> listarOperadores(
            String texto, Boolean activo, Pageable pageable);

    List<OperadorLogisticoResponse> listarActivos();

    ResponseEntity<Object> obtenerPorId(Integer operadorLogisticoId);

    ResponseEntity<Object> validarDuplicados(String ruc, String razonSocial, Integer operadorLogisticoId);

    ResponseEntity<Object> registrar(OperadorLogisticoRegistroRequest request);

    ResponseEntity<Object> cambiarEstado(Integer operadorLogisticoId, Boolean activo);
}
