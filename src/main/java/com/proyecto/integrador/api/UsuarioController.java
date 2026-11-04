package com.proyecto.integrador.api;

import com.proyecto.integrador.utils.MessageResponse;
import com.proyecto.integrador.utils.CustomPage;

import com.proyecto.integrador.model.request.UsuarioRegistroRequest;
import com.proyecto.integrador.model.request.CambiarContraseniaRequest;
import com.proyecto.integrador.model.response.UsuarioResponse;
import com.proyecto.integrador.service.UsuarioService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.security.access.prepost.PreAuthorize;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Validated
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('Administrador') or hasAuthority('PERM_usuarios_consultar')")
    public CustomPage<UsuarioResponse> listarUsuarios(
            @RequestParam(value = "texto", required = false) String texto,
            @RequestParam(value = "activo", required = false) Boolean activo,
            @RequestParam(value = "pagina", defaultValue = "1") Integer pagina,
            @RequestParam(value = "tamPagina", defaultValue = "10") Integer tamanioPagina) {
        Pageable pageable = PageRequest.of(pagina - 1, tamanioPagina);
        return usuarioService.listar(texto, activo, pageable);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('Administrador')")
    public ResponseEntity<MessageResponse> registrarUsuario(@Valid @RequestBody UsuarioRegistroRequest request) {
        return usuarioService.registrar(request);
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('Administrador') or hasAuthority('PERM_usuarios_editar')")
    public ResponseEntity<MessageResponse> actualizarUsuario(@Valid @RequestBody UsuarioRegistroRequest request) {
        return usuarioService.actualizar(request);
    }

    @DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('Administrador')")
    public ResponseEntity<MessageResponse> cambiarEstadoUsuario(
            @NotNull @RequestParam("usuarioId") Integer usuarioId,
            @NotNull @RequestParam("activo") Boolean activo) {
        return usuarioService.cambiarEstado(usuarioId, activo);
    }

    @PutMapping(value = "/contrasenia", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MessageResponse> cambiarContraseniaUsuario(
            @Valid @RequestBody CambiarContraseniaRequest request) {
        return usuarioService.cambiarContrasenia(request);
    }
}

