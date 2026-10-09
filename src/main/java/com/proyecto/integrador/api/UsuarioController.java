package com.proyecto.integrador.api;

import com.proyecto.integrador.model.request.UsuarioRegistroRequest;
import com.proyecto.integrador.model.request.CambiarContraseniaRequest;
import com.proyecto.integrador.service.UsuarioService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.security.access.prepost.PreAuthorize;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.proyecto.integrador.model.response.UsuarioResponse;
import com.proyecto.integrador.utils.CustomPage;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import com.proyecto.integrador.model.request.UsuarioActualizarRequest;
import org.springframework.web.bind.annotation.DeleteMapping;
import com.proyecto.integrador.model.request.PerfilUsuarioActualizarRequest;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping
    @PreAuthorize(
            "hasRole('Administrador') or " +
                    "hasAuthority('PERM_usuarios_consultar')"
    )
    public CustomPage<UsuarioResponse> listarUsuarios(
            @RequestParam(required = false)
            String texto,

            @RequestParam(required = false)
            Integer rolId,

            @RequestParam(required = false)
            Boolean activo,

            @RequestParam(defaultValue = "1")
            Integer pagina,

            @RequestParam(defaultValue = "10")
            Integer tamPagina
    ) {
        int numeroPagina = Math.max(pagina, 1);
        int tamanioPagina = Math.max(tamPagina, 1);

        Pageable pageable = PageRequest.of(
                numeroPagina - 1,
                tamanioPagina
        );

        return usuarioService.listarUsuarios(
                texto,
                rolId,
                activo,
                pageable
        );
    }

    @GetMapping("/{usuarioId}")
    @PreAuthorize(
            "hasRole('Administrador') or " +
                    "hasAuthority('PERM_usuarios_consultar')"
    )
    public ResponseEntity<UsuarioResponse> obtenerPorId(
            @PathVariable Integer usuarioId
    ) {
        UsuarioResponse usuario =
                usuarioService.obtenerPorId(usuarioId);

        if (usuario == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(usuario);
    }

    @PutMapping
    @PreAuthorize(
            "hasRole('Administrador') or " +
                    "hasAuthority('PERM_usuarios_editar')"
    )
    public ResponseEntity<Object> actualizar(
            @Valid
            @RequestBody UsuarioActualizarRequest request
    ) {
        return usuarioService.actualizar(request);
    }

    @DeleteMapping
    @PreAuthorize(
            "hasRole('Administrador') or " +
                    "hasAuthority('PERM_usuarios_eliminar')"
    )
    public ResponseEntity<Object> cambiarEstado(
            @RequestParam Integer usuarioId,
            @RequestParam Boolean activo
    ) {
        return usuarioService.cambiarEstado(
                usuarioId,
                activo
        );
    }

    @PostMapping
    @PreAuthorize("hasRole('Administrador')")
    public ResponseEntity<Object> registrar(@Valid @RequestBody UsuarioRegistroRequest request) {
        return usuarioService.registrar(request);
    }

    @PutMapping("/perfil")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Object> actualizarPerfil(
            @Valid
            @RequestBody
            PerfilUsuarioActualizarRequest request
    ) {
        return usuarioService.actualizarPerfil(request);
    }

    @PutMapping("/contrasenia")
    public ResponseEntity<Object> cambiarContrasenia(
            @Valid @RequestBody CambiarContraseniaRequest request) {
        return usuarioService.cambiarContrasenia(request);
    }
}
