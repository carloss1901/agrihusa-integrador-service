package com.proyecto.integrador.api;

import com.proyecto.integrador.model.request.SituacionRegistroRequest;
import com.proyecto.integrador.model.response.SituacionResponse;
import com.proyecto.integrador.service.SituacionService;
import com.proyecto.integrador.utils.CustomPage;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/situaciones")
@RequiredArgsConstructor
@Validated
public class SituacionController {

    private final SituacionService situacionService;

    @GetMapping
    public CustomPage<SituacionResponse> listarSituaciones(
            @RequestParam(value = "descripcion", required = false) String descripcion,
            @RequestParam(value = "activo", required = false) Boolean activo,
            @RequestParam(value = "pagina", defaultValue = "1") Integer pagina,
            @RequestParam(value = "tamPagina", defaultValue = "10") Integer tamanioPagina) {
        Pageable pageable = PageRequest.of(pagina - 1, tamanioPagina);
        return situacionService.listarSituaciones(descripcion, activo, pageable);
    }

    @DeleteMapping
    public ResponseEntity<Object> cambiarEstado(
            @NotNull(message = "{message.required}") @RequestParam("situacionId") Integer situacionId,
            @NotNull(message = "{message.required}") @RequestParam("activo") Boolean activo) {
        return situacionService.cambiarEstado(situacionId, activo);
    }

    @PostMapping
    public ResponseEntity<Object> registrar(@Valid @RequestBody SituacionRegistroRequest request) {
        return situacionService.registrar(request);
    }
}
