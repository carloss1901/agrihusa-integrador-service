package com.proyecto.integrador.api;

import com.proyecto.integrador.model.request.PuertoLlegadaRegistroRequest;
import com.proyecto.integrador.model.response.PuertoLlegadaResponse;
import com.proyecto.integrador.service.PuertoLlegadaService;
import com.proyecto.integrador.utils.CustomPage;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
@RequestMapping("/api/puertos-llegada")
@RequiredArgsConstructor
@Validated
@SecurityRequirement(name = "bearerAuth")
public class PuertoLlegadaController {

    private final PuertoLlegadaService puertoService;

    @GetMapping
    public CustomPage<PuertoLlegadaResponse> listarPuertos(
            @RequestParam(value = "texto", required = false) String texto,
            @RequestParam(value = "pais", required = false) String pais,
            @RequestParam(value = "activo", required = false) Boolean activo,
            @RequestParam(value = "pagina", defaultValue = "1") Integer pagina,
            @RequestParam(value = "tamPagina", defaultValue = "10") Integer tamanioPagina) {
        Pageable pageable = PageRequest.of(pagina - 1, tamanioPagina);
        return puertoService.listarPuertos(texto, pais, activo, pageable);
    }

    @DeleteMapping
    public ResponseEntity<Object> cambiarEstado(
            @NotNull(message = "{message.required}") @RequestParam("puertoLlegadaId") Integer puertoLlegadaId,
            @NotNull(message = "{message.required}") @RequestParam("activo") Boolean activo) {
        return puertoService.cambiarEstado(puertoLlegadaId, activo);
    }

    @PostMapping
    public ResponseEntity<Object> registrar(@Valid @RequestBody PuertoLlegadaRegistroRequest request) {
        return puertoService.registrar(request);
    }
}
