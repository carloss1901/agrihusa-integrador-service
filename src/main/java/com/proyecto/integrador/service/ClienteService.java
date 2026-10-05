package com.proyecto.integrador.service;

import com.proyecto.integrador.model.request.ClienteRegistroRequest;
import com.proyecto.integrador.model.response.ClienteResponse;
import com.proyecto.integrador.utils.CustomPage;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ClienteService {
    CustomPage<ClienteResponse> listarClientes(String texto, String tipoDocumento, Boolean activo, Pageable pageable);
    List<ClienteResponse> listarActivos();
    ResponseEntity<Object> obtenerPorId(Integer clienteId);
    ResponseEntity<Object> validarDuplicados(String numeroDocumento, String razonSocial, Integer clienteId);
    ResponseEntity<Object> registrar(ClienteRegistroRequest request);
    ResponseEntity<Object> cambiarEstado(Integer clienteId, Boolean activo);
}
