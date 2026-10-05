package com.proyecto.integrador.api;

import com.proyecto.integrador.model.request.UsuarioRegistroRequest;
import com.proyecto.integrador.model.request.CambiarContraseniaRequest;
import com.proyecto.integrador.service.UsuarioService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.security.access.prepost.PreAuthorize;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

    @PostMapping
    @PreAuthorize("hasRole('Administrador')")
    public ResponseEntity<Object> registrar(@Valid @RequestBody UsuarioRegistroRequest request) {
        return usuarioService.registrar(request);
    }

    @PutMapping("/contrasenia")
    public ResponseEntity<Object> cambiarContrasenia(
            @Valid @RequestBody CambiarContraseniaRequest request) {
        return usuarioService.cambiarContrasenia(request);
    }
}
