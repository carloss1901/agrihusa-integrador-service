package com.proyecto.integrador.api;

import com.proyecto.integrador.utils.MessageResponse;

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
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
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
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
@Validated
@SecurityRequirement(name = "bearerAuth")
public class ClienteController {

    private final ClienteService clienteService;

    @PreAuthorize("hasRole('Administrador') or hasAuthority('PERM_clientes_consultar')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public CustomPage<ClienteResponse> listarClientes(
            @RequestParam(value = "texto", required = false) String texto,
            @RequestParam(value = "tipoDocumento", required = false) String tipoDocumento,
            @RequestParam(value = "activo", required = false) Boolean activo,
            @RequestParam(value = "pagina", defaultValue = "1") Integer pagina,
            @RequestParam(value = "tamPagina", defaultValue = "10") Integer tamPagina) {
        Pageable pageable = PageRequest.of(pagina - 1, tamPagina);
        return clienteService.listarClientes(texto, tipoDocumento, activo, pageable);
    }

    @PreAuthorize("hasRole('Administrador') or hasAuthority('PERM_clientes_eliminar')")
    @DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MessageResponse> cambiarEstadoCliente(
            @NotNull(message = "{message.required}") @RequestParam("clienteId") Integer clienteId,
            @NotNull(message = "{message.required}") @RequestParam("activo") Boolean activo) {
        return clienteService.cambiarEstado(clienteId, activo);
    }

    @PreAuthorize("hasRole('Administrador') or hasAuthority('PERM_clientes_crear')")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MessageResponse> registrarCliente(@Valid @RequestBody ClienteRegistroRequest request) {
        return clienteService.registrar(request);
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('Administrador') or hasAuthority('PERM_clientes_editar')")
    public ResponseEntity<MessageResponse> actualizarCliente(@Valid @RequestBody ClienteRegistroRequest request) {
        return clienteService.actualizar(request);
    }
}

