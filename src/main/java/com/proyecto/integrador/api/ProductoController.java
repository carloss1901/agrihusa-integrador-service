package com.proyecto.integrador.api;

import com.proyecto.integrador.model.request.ProductoRegistroRequest;
import com.proyecto.integrador.model.response.ProductoResponse;
import com.proyecto.integrador.service.ProductoService;
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
@RequestMapping("/api/productos")
@RequiredArgsConstructor
@Validated
public class ProductoController {

    private final ProductoService productoService;

    @GetMapping
    public CustomPage<ProductoResponse> listarProductos(
            @RequestParam(value = "texto", required = false) String texto,
            @RequestParam(value = "activo", required = false) Boolean activo,
            @RequestParam(value = "pagina", defaultValue = "1") Integer pagina,
            @RequestParam(value = "tamPagina", defaultValue = "10") Integer tamanioPagina) {
        Pageable pageable = PageRequest.of(pagina - 1, tamanioPagina);
        return productoService.listarProductos(texto, activo, pageable);
    }

    @DeleteMapping
    public ResponseEntity<Object> cambiarEstado(
            @NotNull(message = "{message.required}") @RequestParam("productoId") Integer productoId,
            @NotNull(message = "{message.required}") @RequestParam("activo") Boolean activo) {
        return productoService.cambiarEstado(productoId, activo);
    }

    @PostMapping
    public ResponseEntity<Object> registrar(@Valid @RequestBody ProductoRegistroRequest request) {
        return productoService.registrar(request);
    }
}
