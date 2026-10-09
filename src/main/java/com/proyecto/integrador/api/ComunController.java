package com.proyecto.integrador.api;

import com.proyecto.integrador.model.response.ComunResponse;
import com.proyecto.integrador.repository.ClienteRepository;
import com.proyecto.integrador.repository.DestinoRepository;
import com.proyecto.integrador.repository.NavieraRepository;
import com.proyecto.integrador.repository.OperadorLogisticoRepository;
import com.proyecto.integrador.repository.PuertoLlegadaRepository;
import com.proyecto.integrador.repository.SituacionRepository;
import com.proyecto.integrador.repository.ViaRepository;
import com.proyecto.integrador.repository.VariedadRepository;
import com.proyecto.integrador.service.RolService;
import com.proyecto.integrador.service.ProductoService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/comun")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class ComunController {

    private final RolService rolService;
    private final ProductoService productoService;
    private final ClienteRepository clienteRepository;
    private final OperadorLogisticoRepository operadorLogisticoRepository;
    private final ViaRepository viaRepository;
    private final NavieraRepository navieraRepository;
    private final DestinoRepository destinoRepository;
    private final PuertoLlegadaRepository puertoLlegadaRepository;
    private final SituacionRepository situacionRepository;
    private final VariedadRepository variedadRepository;

    @GetMapping(value = "/roles-activos", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated()")
    public List<ComunResponse> listarRolesActivos() {
        return rolService.listarRolesActivosCombo();
    }

    @GetMapping(value = "/productos-activos", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated()")
    public List<ComunResponse> listarProductosActivos() {
        return productoService.listarProductosActivosCombo();
    }

    @GetMapping(value = "/clientes-activos", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated()")
    public List<ComunResponse> listarClientesActivos() {
        return clienteRepository.findAllByActivoTrueOrderByRazonSocialAsc().stream()
                .map(item -> new ComunResponse(item.getClienteId(), item.getRazonSocial()))
                .toList();
    }

    @GetMapping(value = "/operadores-logisticos-activos", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated()")
    public List<ComunResponse> listarOperadoresLogisticosActivos() {
        return operadorLogisticoRepository.findAllByActivoTrueOrderByRazonSocialAsc().stream()
                .map(item -> new ComunResponse(item.getOperadorLogisticoId(), item.getRazonSocial()))
                .toList();
    }

    @GetMapping(value = "/vias-activos", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated()")
    public List<ComunResponse> listarViasActivos() {
        return viaRepository.findAllByActivoTrueOrderByDescripcionAsc().stream()
                .map(item -> new ComunResponse(item.getViaId(), item.getDescripcion()))
                .toList();
    }

    @GetMapping(value = "/navieras-activas", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated()")
    public List<ComunResponse> listarNavierasActivas() {
        return navieraRepository.findAllByActivoTrueOrderByNombreAsc().stream()
                .map(item -> new ComunResponse(item.getNavieraId(), item.getNombre()))
                .toList();
    }

    @GetMapping(value = "/destinos-activos", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated()")
    public List<ComunResponse> listarDestinosActivos() {
        return destinoRepository.findAllByActivoTrueOrderByCiudadAsc().stream()
                .map(item -> new ComunResponse(item.getDestinoId(), item.getCiudad() + ", " + item.getPais()))
                .toList();
    }

    @GetMapping(value = "/puertos-llegada-activos", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated()")
    public List<ComunResponse> listarPuertosLlegadaActivos() {
        return puertoLlegadaRepository.findAllByActivoTrueOrderByPuertoAsc().stream()
                .map(item -> new ComunResponse(item.getPuertoLlegadaId(), item.getPuerto()))
                .toList();
    }

    @GetMapping(value = "/situaciones-activas", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated()")
    public List<ComunResponse> listarSituacionesActivas() {
        return situacionRepository.findAllByActivoTrueOrderByDescripcionAsc().stream()
                .map(item -> new ComunResponse(item.getSituacionId(), item.getDescripcion()))
                .toList();
    }

    @GetMapping(value = "/variedades-activas", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated()")
    public List<ComunResponse> listarVariedadesActivas() {
        return variedadRepository.findAllByActivoTrueOrderByNombreAsc().stream()
                .map(item -> new ComunResponse(item.getVariedadId(), item.getNombre(), item.getProductoId()))
                .toList();
    }
}
