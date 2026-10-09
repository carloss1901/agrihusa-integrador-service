package com.proyecto.integrador.api;

import com.proyecto.integrador.model.request.BitacoraRegistroRequest;
import com.proyecto.integrador.model.response.BitacoraResponse;
import com.proyecto.integrador.service.BitacoraService;
import com.proyecto.integrador.utils.CustomPage;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.security.access.prepost.PreAuthorize;
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
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@RestController
@RequestMapping("/api/bitacoras")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class BitacoraController {

    private final BitacoraService bitacoraService;

    @GetMapping
    @PreAuthorize(
            "hasRole('Administrador') or " +
                    "hasAuthority('PERM_bitacora_consultar')"
    )
    public CustomPage<BitacoraResponse> listar(
            @RequestParam(required = false)
            String usuario,

            @RequestParam(required = false)
            String modulo,

            @RequestParam(required = false)
            String accion,

            @RequestParam(required = false)
            String entidad,

            @RequestParam(required = false)
            String resultado,

            @RequestParam(required = false)
            Boolean activo,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fechaDesde,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fechaHasta,

            @RequestParam(defaultValue = "1")
            Integer pagina,

            @RequestParam(defaultValue = "10")
            Integer tamPagina
    ) {
        LocalDateTime inicio =
                fechaDesde == null
                        ? null
                        : fechaDesde.atStartOfDay();

        LocalDateTime fin =
                fechaHasta == null
                        ? null
                        : fechaHasta.atTime(
                        LocalTime.MAX
                );

        Pageable pageable = PageRequest.of(
                Math.max(pagina, 1) - 1,
                Math.max(tamPagina, 1)
        );

        return bitacoraService.listarBitacoras(
                usuario,
                modulo,
                accion,
                entidad,
                resultado,
                activo,
                inicio,
                fin,
                pageable
        );
    }

    @PostMapping
    @PreAuthorize("hasRole('Administrador') or hasAuthority('PERM_bitacora_crear')")
    public ResponseEntity<Object> registrar(@Valid @RequestBody BitacoraRegistroRequest request) {
        return bitacoraService.registrar(request);
    }
}
