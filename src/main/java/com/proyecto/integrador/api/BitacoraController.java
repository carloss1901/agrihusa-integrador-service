package com.proyecto.integrador.api;

import com.proyecto.integrador.model.request.BitacoraRegistroRequest;
import com.proyecto.integrador.model.response.BitacoraResponse;
import com.proyecto.integrador.service.BitacoraService;
import com.proyecto.integrador.utils.CustomPage;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bitacoras")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class BitacoraController {

    private final BitacoraService bitacoraService;

    @GetMapping
    public CustomPage<BitacoraResponse> listar(
            @RequestParam(value = "usuarioId", required = false) Integer usuarioId,
            @RequestParam(value = "modulo", required = false) String modulo,
            @RequestParam(value = "accion", required = false) String accion,
            @RequestParam(value = "entidad", required = false) String entidad,
            @RequestParam(value = "resultado", required = false) String resultado,
            @RequestParam(value = "activo", required = false) Boolean activo,
            @RequestParam(value = "pagina", defaultValue = "1") Integer pagina,
            @RequestParam(value = "tamPagina", defaultValue = "10") Integer tamPagina) {
        Pageable pageable = PageRequest.of(pagina - 1, tamPagina);
        return bitacoraService.listarBitacoras(usuarioId, modulo, accion, entidad, resultado, activo, pageable);
    }

    @PostMapping
    public ResponseEntity<Object> registrar(@Valid @RequestBody BitacoraRegistroRequest request) {
        return bitacoraService.registrar(request);
    }
}
