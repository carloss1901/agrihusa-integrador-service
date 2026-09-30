package com.proyecto.integrador.api;

import com.proyecto.integrador.model.request.OperadorLogisticoRegistroRequest;
import com.proyecto.integrador.model.response.OperadorLogisticoResponse;
import com.proyecto.integrador.service.OperadorLogisticoService;
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
@RequestMapping("/api/operadores-logisticos")
@RequiredArgsConstructor
@Validated
@SecurityRequirement(name = "bearerAuth")
public class OperadorLogisticoController {

    private final OperadorLogisticoService operadorService;

    @GetMapping
    public CustomPage<OperadorLogisticoResponse> listarOperadores(
            @RequestParam(value = "texto", required = false) String texto,
            @RequestParam(value = "activo", required = false) Boolean activo,
            @RequestParam(value = "pagina", defaultValue = "1") Integer pagina,
            @RequestParam(value = "tamPagina", defaultValue = "10") Integer tamanioPagina) {
        Pageable pageable = PageRequest.of(pagina - 1, tamanioPagina);
        return operadorService.listarOperadores(texto, activo, pageable);
    }

    @DeleteMapping
    public ResponseEntity<Object> cambiarEstado(
            @NotNull(message = "{message.required}") @RequestParam("operadorLogisticoId") Integer operadorLogisticoId,
            @NotNull(message = "{message.required}") @RequestParam("activo") Boolean activo) {
        return operadorService.cambiarEstado(operadorLogisticoId, activo);
    }

    @PostMapping
    public ResponseEntity<Object> registrar(@Valid @RequestBody OperadorLogisticoRegistroRequest request) {
        return operadorService.registrar(request);
    }
}
