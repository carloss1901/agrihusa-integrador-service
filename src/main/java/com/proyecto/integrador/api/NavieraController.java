package com.proyecto.integrador.api;

import com.proyecto.integrador.model.request.NavieraRegistroRequest;
import com.proyecto.integrador.model.response.NavieraResponse;
import com.proyecto.integrador.service.NavieraService;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController @RequestMapping("/api/navieras") @RequiredArgsConstructor @Validated
@SecurityRequirement(name = "bearerAuth")
public class NavieraController {
    private final NavieraService navieraService;
    @GetMapping public CustomPage<NavieraResponse> listar(@RequestParam(value="texto", required=false) String texto,
            @RequestParam(value="pais", required=false) String pais, @RequestParam(value="activo", required=false) Boolean activo,
            @RequestParam(value="pagina", defaultValue="1") Integer pagina, @RequestParam(value="tamPagina", defaultValue="10") Integer tamPagina) {
        Pageable pageable = PageRequest.of(pagina - 1, tamPagina); return navieraService.listarNavieras(texto, pais, activo, pageable);
    }
    @GetMapping("/activas") public List<NavieraResponse> listarActivas() { return navieraService.listarActivas(); }
    @GetMapping("/validar") public ResponseEntity<Object> validarDuplicados(@RequestParam(value="codigo", required=false) String codigo,
            @RequestParam(value="nombre", required=false) String nombre, @RequestParam(value="navieraId", required=false) Integer navieraId) {
        return navieraService.validarDuplicados(codigo, nombre, navieraId);
    }
    @GetMapping("/{navieraId}") public ResponseEntity<Object> obtenerPorId(@PathVariable("navieraId") Integer navieraId) { return navieraService.obtenerPorId(navieraId); }
    @DeleteMapping public ResponseEntity<Object> cambiarEstado(@NotNull(message="{message.required}") @RequestParam("navieraId") Integer navieraId,
            @NotNull(message="{message.required}") @RequestParam("activo") Boolean activo) { return navieraService.cambiarEstado(navieraId, activo); }
    @PostMapping public ResponseEntity<Object> registrar(@Valid @RequestBody NavieraRegistroRequest request) { return navieraService.registrar(request); }
}
