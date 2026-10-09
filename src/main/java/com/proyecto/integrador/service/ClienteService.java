package com.proyecto.integrador.service;

import com.proyecto.integrador.model.request.ClienteRegistroRequest;
import com.proyecto.integrador.model.response.ClienteResponse;
import com.proyecto.integrador.utils.CustomPage;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface ClienteService {
    CustomPage<ClienteResponse> listarClientes(String texto, String tipoDocumento, Boolean activo, Pageable pageable);
    ResponseEntity<Object> registrar(ClienteRegistroRequest request);
    default ResponseEntity<Object> actualizar(ClienteRegistroRequest request) {
        return registrar(request);
    }
    ResponseEntity<Object> cambiarEstado(Integer clienteId, Boolean activo);
}
