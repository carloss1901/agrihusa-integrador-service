package com.proyecto.integrador.api;

import com.proyecto.integrador.model.request.RolRegistroRequest;
import com.proyecto.integrador.model.response.RolResponse;
import com.proyecto.integrador.service.RolService;
import com.proyecto.integrador.utils.CustomPage;
import com.proyecto.integrador.utils.MessageResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.security.access.prepost.PreAuthorize;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
@Validated
@SecurityRequirement(name = "bearerAuth")
public class RolController {

    private final RolService rolService;

    @GetMapping
    @PreAuthorize("hasRole('Administrador') or hasAuthority('PERM_roles_consultar')")
    public CustomPage<RolResponse> listarRoles(
            @RequestParam(value = "nombre", required = false) String nombre,
            @RequestParam(value = "activo", required = false) Boolean activo,
            @RequestParam(value = "pagina", defaultValue = "1") Integer pagina,
            @RequestParam(value = "tamPagina", defaultValue = "10") Integer tamanioPagina) {
        Pageable pageable = PageRequest.of(pagina - 1, tamanioPagina);
        return rolService.listarRoles(nombre, activo, pageable);
    }

    @DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('Administrador')")
    public ResponseEntity<MessageResponse> cambiarEstado(
            @NotNull(message = "{message.required}") @RequestParam("rolId") Integer rolId,
            @NotNull(message = "{message.required}") @RequestParam("activo") Boolean activo) {
        return rolService.cambiarEstado(rolId, activo);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('Administrador')")
    public ResponseEntity<MessageResponse> registrar(@Valid @RequestBody RolRegistroRequest request) {
        return rolService.registrar(request);
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('Administrador') or hasAuthority('PERM_roles_editar')")
    public ResponseEntity<MessageResponse> actualizar(@Valid @RequestBody RolRegistroRequest request) {
        return rolService.actualizar(request);
    }
}

