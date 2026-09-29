package com.proyecto.integrador.api;

import com.proyecto.integrador.model.request.DespachoRegistroRequest;
import com.proyecto.integrador.model.response.DespachoResponse;
import com.proyecto.integrador.service.DespachoService;
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
@RequestMapping("/api/despachos")
@RequiredArgsConstructor
@Validated
public class DespachoController {

    private final DespachoService despachoService;

    @GetMapping
    public CustomPage<DespachoResponse> listar(
            @RequestParam(value = "texto", required = false) String texto,
            @RequestParam(value = "clienteId", required = false) Integer clienteId,
            @RequestParam(value = "situacionId", required = false) Integer situacionId,
            @RequestParam(value = "activo", required = false) Boolean activo,
            @RequestParam(value = "pagina", defaultValue = "1") Integer pagina,
            @RequestParam(value = "tamPagina", defaultValue = "10") Integer tamPagina) {
        Pageable pageable = PageRequest.of(pagina - 1, tamPagina);
        return despachoService.listarDespachos(texto, clienteId, situacionId, activo, pageable);
    }

    @PostMapping
    public ResponseEntity<Object> registrar(@Valid @RequestBody DespachoRegistroRequest request) {
        return despachoService.registrar(request);
    }

    @DeleteMapping
    public ResponseEntity<Object> cambiarEstado(
            @NotNull(message = "{message.required}") @RequestParam("despachoId") Integer despachoId,
            @NotNull(message = "{message.required}") @RequestParam("activo") Boolean activo) {
        return despachoService.cambiarEstado(despachoId, activo);
    }
}
