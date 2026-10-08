package com.proyecto.integrador.api;

import com.proyecto.integrador.utils.MessageResponse;

import com.proyecto.integrador.model.request.NavieraRegistroRequest;
import com.proyecto.integrador.model.response.NavieraResponse;
import com.proyecto.integrador.service.NavieraService;
import com.proyecto.integrador.utils.CustomPage;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/navieras")
@RequiredArgsConstructor
@Validated
@SecurityRequirement(name = "bearerAuth")
public class NavieraController {

    private final NavieraService navieraService;

    @PreAuthorize("hasRole('Administrador') or hasAuthority('PERM_navieras_consultar')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public CustomPage<NavieraResponse> listar(
            @RequestParam(value = "texto", required = false) String texto,
            @RequestParam(value = "pais", required = false) String pais,
            @RequestParam(value = "activo", required = false) Boolean activo,
            @RequestParam(value = "pagina", defaultValue = "1") Integer pagina,
            @RequestParam(value = "tamPagina", defaultValue = "10") Integer tamPagina) {
        Pageable pageable = PageRequest.of(pagina - 1, tamPagina);
        return navieraService.listarNavieras(texto, pais, activo, pageable);
    }

    @PreAuthorize("hasRole('Administrador') or hasAuthority('PERM_navieras_eliminar')")
    @DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MessageResponse> cambiarEstado(
            @NotNull(message = "{message.required}") @RequestParam("navieraId") Integer navieraId,
            @NotNull(message = "{message.required}") @RequestParam("activo") Boolean activo) {
        return navieraService.cambiarEstado(navieraId, activo);
    }

    @PreAuthorize("hasRole('Administrador') or hasAuthority('PERM_navieras_crear')")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MessageResponse> registrar(@Valid @RequestBody NavieraRegistroRequest request) {
        return navieraService.registrar(request);
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('Administrador') or hasAuthority('PERM_navieras_editar')")
    public ResponseEntity<MessageResponse> actualizar(@Valid @RequestBody NavieraRegistroRequest request) {
        return navieraService.actualizar(request);
    }
}

