package com.proyecto.integrador.service;

import com.proyecto.integrador.model.request.LoginRequest;
import com.proyecto.integrador.utils.MessageResponse;
import org.springframework.http.ResponseEntity;

public interface LoginService {
    ResponseEntity<MessageResponse> login(LoginRequest request);
}
