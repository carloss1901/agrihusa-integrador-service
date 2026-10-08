package com.proyecto.integrador.service;

import com.proyecto.integrador.utils.MessageResponse;

import com.proyecto.integrador.model.response.UsuarioResponse;
import com.proyecto.integrador.utils.CustomPage;
import com.proyecto.integrador.model.request.UsuarioRegistroRequest;
import com.proyecto.integrador.model.request.CambiarContraseniaRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Pageable;

public interface UsuarioService {

    ResponseEntity<MessageResponse> registrar(UsuarioRegistroRequest request);

    CustomPage<UsuarioResponse> listar(String texto, Boolean activo, Pageable pageable);

    ResponseEntity<MessageResponse> actualizar(UsuarioRegistroRequest request);

    ResponseEntity<MessageResponse> cambiarEstado(Integer usuarioId, Boolean activo);

    ResponseEntity<MessageResponse> cambiarContrasenia(CambiarContraseniaRequest request);
}

