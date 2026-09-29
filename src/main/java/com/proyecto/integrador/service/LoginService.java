package com.proyecto.integrador.service;

import com.proyecto.integrador.model.request.LoginRequest;
import org.springframework.http.ResponseEntity;

public interface LoginService {
    ResponseEntity<Object> login(LoginRequest request);
}
