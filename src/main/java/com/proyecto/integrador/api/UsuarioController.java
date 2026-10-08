package com.proyecto.integrador.api;

import com.proyecto.integrador.utils.MessageResponse;

import com.proyecto.integrador.model.request.UsuarioRegistroRequest;
import com.proyecto.integrador.model.request.CambiarContraseniaRequest;
import com.proyecto.integrador.service.UsuarioService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.security.access.prepost.PreAuthorize;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('Administrador')")
    public ResponseEntity<MessageResponse> registrar(@Valid @RequestBody UsuarioRegistroRequest request) {
        return usuarioService.registrar(request);
    }

    @PutMapping(value = "/contrasenia", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MessageResponse> cambiarContrasenia(
            @Valid @RequestBody CambiarContraseniaRequest request) {
        return usuarioService.cambiarContrasenia(request);
    }
}

