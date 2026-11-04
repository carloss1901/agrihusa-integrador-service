package com.proyecto.integrador.api;

import com.proyecto.integrador.model.response.ClimaResponse;
import com.proyecto.integrador.service.ClimaService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/meteorologia")
@RequiredArgsConstructor
@Validated
public class ClimaController {

    private final ClimaService climaService;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ClimaResponse consultar(
            @RequestParam
            @NotBlank(message = "El destino es obligatorio")
            @Size(max = 120, message = "El destino no debe superar 120 caracteres")
            String destino
    ) {
        return climaService.consultar(destino.trim());
    }
}