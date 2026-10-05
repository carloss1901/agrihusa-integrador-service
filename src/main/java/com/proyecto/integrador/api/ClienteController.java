package com.proyecto.integrador.api;

import com.proyecto.integrador.model.request.ClienteRegistroRequest;
import com.proyecto.integrador.model.response.ClienteResponse;
import com.proyecto.integrador.service.ClienteService;
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

@RestController @RequestMapping("/api/clientes") @RequiredArgsConstructor @Validated
@SecurityRequirement(name = "bearerAuth")
public class ClienteController {
    private final ClienteService clienteService;
    @GetMapping public CustomPage<ClienteResponse> listar(@RequestParam(value="texto", required=false) String texto,
            @RequestParam(value="tipoDocumento", required=false) String tipoDocumento, @RequestParam(value="activo", required=false) Boolean activo,
            @RequestParam(value="pagina", defaultValue="1") Integer pagina, @RequestParam(value="tamPagina", defaultValue="10") Integer tamPagina) {
        Pageable pageable = PageRequest.of(pagina - 1, tamPagina); return clienteService.listarClientes(texto, tipoDocumento, activo, pageable);
    }
    @GetMapping("/activos") public List<ClienteResponse> listarActivos() { return clienteService.listarActivos(); }
    @GetMapping("/validar") public ResponseEntity<Object> validarDuplicados(@RequestParam(value="numeroDocumento", required=false) String numeroDocumento,
            @RequestParam(value="razonSocial", required=false) String razonSocial, @RequestParam(value="clienteId", required=false) Integer clienteId) {
        return clienteService.validarDuplicados(numeroDocumento, razonSocial, clienteId);
    }
    @GetMapping("/{clienteId}") public ResponseEntity<Object> obtenerPorId(@PathVariable("clienteId") Integer clienteId) { return clienteService.obtenerPorId(clienteId); }
    @DeleteMapping public ResponseEntity<Object> cambiarEstado(@NotNull(message="{message.required}") @RequestParam("clienteId") Integer clienteId,
            @NotNull(message="{message.required}") @RequestParam("activo") Boolean activo) { return clienteService.cambiarEstado(clienteId, activo); }
    @PostMapping public ResponseEntity<Object> registrar(@Valid @RequestBody ClienteRegistroRequest request) { return clienteService.registrar(request); }
}
