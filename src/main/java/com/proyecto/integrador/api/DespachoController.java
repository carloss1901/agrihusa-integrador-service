package com.proyecto.integrador.api;

import com.proyecto.integrador.utils.MessageResponse;

import com.proyecto.integrador.model.request.DespachoRegistroRequest;
import com.proyecto.integrador.model.response.DespachoResponse;
import com.proyecto.integrador.service.DespachoService;
import com.proyecto.integrador.utils.CustomPage;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.security.access.prepost.PreAuthorize;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
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
@RequestMapping("/api/despachos")
@RequiredArgsConstructor
@Validated
@SecurityRequirement(name = "bearerAuth")
public class DespachoController {

    private final DespachoService despachoService;

    @GetMapping
    @PreAuthorize("hasRole('Administrador') or hasAuthority('PERM_despachos_consultar')")
    public CustomPage<DespachoResponse> listarDespachos(
            @RequestParam(value = "texto", required = false) String texto,
            @RequestParam(value = "clienteId", required = false) Integer clienteId,
            @RequestParam(value = "situacionId", required = false) Integer situacionId,
            @RequestParam(value = "activo", required = false) Boolean activo,
            @RequestParam(value = "pagina", defaultValue = "1") Integer pagina,
            @RequestParam(value = "tamPagina", defaultValue = "10") Integer tamPagina) {
        Pageable pageable = PageRequest.of(pagina - 1, tamPagina);
        return despachoService.listarDespachos(texto, clienteId, situacionId, activo, pageable);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('Administrador') or hasAuthority('PERM_despachos_crear')")
    public ResponseEntity<MessageResponse> registrarDespacho(@Valid @RequestBody DespachoRegistroRequest request) {
        return despachoService.registrar(request);
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('Administrador') or hasAuthority('PERM_despachos_editar')")
    public ResponseEntity<MessageResponse> actualizarDespacho(@Valid @RequestBody DespachoRegistroRequest request) {
        return despachoService.actualizar(request);
    }

    @DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('Administrador') or hasAuthority('PERM_despachos_eliminar')")
    public ResponseEntity<MessageResponse> cambiarEstadoDespacho(
            @NotNull(message = "{message.required}") @RequestParam("despachoId") Integer despachoId,
            @NotNull(message = "{message.required}") @RequestParam("activo") Boolean activo) {
        return despachoService.cambiarEstado(despachoId, activo);
    }
}

