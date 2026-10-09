package com.proyecto.integrador.api;

import com.proyecto.integrador.model.request.DestinoRegistroRequest;
import com.proyecto.integrador.model.response.DestinoResponse;
import com.proyecto.integrador.service.DestinoService;
import com.proyecto.integrador.utils.CustomPage;
import com.proyecto.integrador.utils.MessageResponse;
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
@RequestMapping("/api/destinos")
@RequiredArgsConstructor
@Validated
@SecurityRequirement(name = "bearerAuth")
public class DestinoController {

    private final DestinoService destinoService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('Administrador') or hasAuthority('PERM_destinos_consultar')")
    public CustomPage<DestinoResponse> listarDestinos(
            @RequestParam(value = "pais", required = false) String pais,
            @RequestParam(value = "ciudad", required = false) String ciudad,
            @RequestParam(value = "activo", required = false) Boolean activo,
            @RequestParam(value = "pagina", defaultValue = "1") Integer pagina,
            @RequestParam(value = "tamPagina", defaultValue = "10") Integer tamanioPagina) {
        Pageable pageable = PageRequest.of(pagina - 1, tamanioPagina);
        return destinoService.listarDestinos(pais, ciudad, activo, pageable);
    }

    @DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('Administrador') or hasAuthority('PERM_destinos_eliminar')")
    public ResponseEntity<MessageResponse> cambiarEstadoDestino(
            @NotNull(message = "{message.required}") @RequestParam("destinoId") Integer destinoId,
            @NotNull(message = "{message.required}") @RequestParam("activo") Boolean activo) {
        return destinoService.cambiarEstado(destinoId, activo);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('Administrador') or hasAuthority('PERM_destinos_crear')")
    public ResponseEntity<MessageResponse> registrarDestino(@Valid @RequestBody DestinoRegistroRequest request) {
        return destinoService.registrar(request);
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('Administrador') or hasAuthority('PERM_destinos_editar')")
    public ResponseEntity<MessageResponse> actualizarDestino(@Valid @RequestBody DestinoRegistroRequest request) {
        return destinoService.actualizar(request);
    }
}
