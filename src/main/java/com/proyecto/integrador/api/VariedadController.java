package com.proyecto.integrador.api;

import com.proyecto.integrador.model.request.VariedadRegistroRequest;
import com.proyecto.integrador.model.response.VariedadResponse;
import com.proyecto.integrador.service.VariedadService;
import com.proyecto.integrador.utils.CustomPage;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.security.access.prepost.PreAuthorize;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/api/variedades")
@RequiredArgsConstructor
@Validated
@SecurityRequirement(name = "bearerAuth")
public class VariedadController {

    private final VariedadService variedadService;

    @GetMapping
    @PreAuthorize("hasRole('Administrador') or hasAuthority('PERM_variedades_consultar')")
    public CustomPage<VariedadResponse> listarVariedades(
            @RequestParam(value = "texto", required = false) String texto,
            @RequestParam(value = "productoId", required = false) Integer productoId,
            @RequestParam(value = "activo", required = false) Boolean activo,
            @RequestParam(value = "pagina", defaultValue = "1") Integer pagina,
            @RequestParam(value = "tamPagina", defaultValue = "10") Integer tamanioPagina) {
        Pageable pageable = PageRequest.of(pagina - 1, tamanioPagina);
        return variedadService.listarVariedades(texto, productoId, activo, pageable);
    }

    @DeleteMapping
    @PreAuthorize("hasRole('Administrador') or hasAuthority('PERM_variedades_eliminar')")
    public ResponseEntity<Object> cambiarEstado(
            @NotNull(message = "{message.required}") @RequestParam("variedadId") Integer variedadId,
            @NotNull(message = "{message.required}") @RequestParam("activo") Boolean activo) {
        return variedadService.cambiarEstado(variedadId, activo);
    }

    @PostMapping
    @PreAuthorize("hasRole('Administrador') or hasAuthority('PERM_variedades_crear')")
    public ResponseEntity<Object> registrar(@Valid @RequestBody VariedadRegistroRequest request) {
        return variedadService.registrar(request);
    }

    @PutMapping
    @PreAuthorize("hasRole('Administrador') or hasAuthority('PERM_variedades_editar')")
    public ResponseEntity<Object> actualizar(@Valid @RequestBody VariedadRegistroRequest request) {
        return variedadService.actualizar(request);
    }
}
