package com.proyecto.integrador.api;

import com.proyecto.integrador.model.response.ClimaResponse;
import com.proyecto.integrador.service.ClimaService;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/clima")
@RequiredArgsConstructor
@Validated
public class ClimaController {
    private final ClimaService climaService;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ClimaResponse consultar(
            @RequestParam @DecimalMin(value = "-90") @DecimalMax(value = "90") double latitud,
            @RequestParam @DecimalMin(value = "-180") @DecimalMax(value = "180") double longitud) {
        return climaService.consultar(latitud, longitud);
    }
}
