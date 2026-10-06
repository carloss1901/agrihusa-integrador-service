package com.proyecto.integrador.api;

import com.proyecto.integrador.model.request.LoginRequest;
import com.proyecto.integrador.service.LoginService;
import com.proyecto.integrador.utils.MessageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/login")
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @PostMapping
    public ResponseEntity<MessageResponse> login(@Valid @RequestBody LoginRequest request) {
        return loginService.login(request);
    }
}
