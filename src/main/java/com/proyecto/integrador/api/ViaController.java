package com.proyecto.integrador.api;

import com.proyecto.integrador.utils.MessageResponse;

import com.proyecto.integrador.model.request.ViaRegistroRequest;
import com.proyecto.integrador.model.response.ViaResponse;
import com.proyecto.integrador.service.ViaService;
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
@RequestMapping("/api/vias")
@RequiredArgsConstructor
@Validated
@SecurityRequirement(name = "bearerAuth")
public class ViaController {

    private final ViaService viaService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('Administrador') or hasAuthority('PERM_vias_consultar')")
    public CustomPage<ViaResponse> listarVias(
            @RequestParam(value = "descripcion", required = false) String descripcion,
            @RequestParam(value = "activo", required = false) Boolean activo,
            @RequestParam(value = "pagina", defaultValue = "1") Integer pagina,
            @RequestParam(value = "tamPagina", defaultValue = "10") Integer tamanioPagina) {
        Pageable pageable = PageRequest.of(pagina - 1, tamanioPagina);
        return viaService.listarVias(descripcion, activo, pageable);
    }

    @DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('Administrador') or hasAuthority('PERM_vias_eliminar')")
    public ResponseEntity<MessageResponse> cambiarEstadoVia(
            @NotNull(message = "{message.required}") @RequestParam("viaId") Integer viaId,
            @NotNull(message = "{message.required}") @RequestParam("activo") Boolean activo) {
        return viaService.cambiarEstado(viaId, activo);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('Administrador') or hasAuthority('PERM_vias_crear')")
    public ResponseEntity<MessageResponse> registrarVia(@Valid @RequestBody ViaRegistroRequest request) {
        return viaService.registrar(request);
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('Administrador') or hasAuthority('PERM_vias_editar')")
    public ResponseEntity<MessageResponse> actualizarVia(@Valid @RequestBody ViaRegistroRequest request) {
        return viaService.actualizar(request);
    }
}

