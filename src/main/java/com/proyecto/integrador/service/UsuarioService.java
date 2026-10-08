package com.proyecto.integrador.service;

import com.proyecto.integrador.utils.MessageResponse;

import com.proyecto.integrador.model.entity.UsuarioEntity;
import com.proyecto.integrador.model.request.UsuarioRegistroRequest;
import com.proyecto.integrador.model.request.CambiarContraseniaRequest;
import org.springframework.http.ResponseEntity;

public interface UsuarioService {

    ResponseEntity<MessageResponse> registrar(UsuarioRegistroRequest request);

    ResponseEntity<MessageResponse> cambiarContrasenia(CambiarContraseniaRequest request);
}

