package com.proyecto.integrador.api;

import com.proyecto.integrador.model.response.ComunResponse;
import com.proyecto.integrador.service.RolService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/comun")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class ComunController {

    private final RolService rolService;

    @GetMapping(value = "/roles-activos", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated()")
    public List<ComunResponse> listarRolesActivos() {
        return rolService.listarRolesActivosCombo();
    }
}
