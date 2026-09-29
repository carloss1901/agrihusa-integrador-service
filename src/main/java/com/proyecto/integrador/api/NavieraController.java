package com.proyecto.integrador.api;

import com.proyecto.integrador.model.request.NavieraRegistroRequest;
import com.proyecto.integrador.model.response.NavieraResponse;
import com.proyecto.integrador.service.NavieraService;
import com.proyecto.integrador.utils.CustomPage;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/navieras") @RequiredArgsConstructor @Validated
public class NavieraController {
    private final NavieraService navieraService;
    @GetMapping public CustomPage<NavieraResponse> listar(@RequestParam(value="texto", required=false) String texto,
            @RequestParam(value="pais", required=false) String pais, @RequestParam(value="activo", required=false) Boolean activo,
            @RequestParam(value="pagina", defaultValue="1") Integer pagina, @RequestParam(value="tamPagina", defaultValue="10") Integer tamPagina) {
        Pageable pageable = PageRequest.of(pagina - 1, tamPagina); return navieraService.listarNavieras(texto, pais, activo, pageable);
    }
    @DeleteMapping public ResponseEntity<Object> cambiarEstado(@NotNull(message="{message.required}") @RequestParam("navieraId") Integer navieraId,
            @NotNull(message="{message.required}") @RequestParam("activo") Boolean activo) { return navieraService.cambiarEstado(navieraId, activo); }
    @PostMapping public ResponseEntity<Object> registrar(@Valid @RequestBody NavieraRegistroRequest request) { return navieraService.registrar(request); }
}
