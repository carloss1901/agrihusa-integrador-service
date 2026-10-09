package com.proyecto.integrador.service.impl;

import com.proyecto.integrador.model.entity.DespachoEntity;
import com.proyecto.integrador.model.request.DespachoRegistroRequest;
import com.proyecto.integrador.model.response.DespachoResponse;
import com.proyecto.integrador.repository.ClienteRepository;
import com.proyecto.integrador.repository.DespachoRepository;
import com.proyecto.integrador.repository.DestinoRepository;
import com.proyecto.integrador.repository.NavieraRepository;
import com.proyecto.integrador.repository.OperadorLogisticoRepository;
import com.proyecto.integrador.repository.ProductoRepository;
import com.proyecto.integrador.repository.PuertoLlegadaRepository;
import com.proyecto.integrador.repository.SituacionRepository;
import com.proyecto.integrador.repository.VariedadRepository;
import com.proyecto.integrador.repository.ViaRepository;
import com.proyecto.integrador.service.DespachoService;
import com.proyecto.integrador.utils.CustomPage;
import com.proyecto.integrador.utils.MessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class DespachoServiceImpl implements DespachoService {

    private static final String MSG_CODIGO = "El código de despacho ya está registrado";
    private static final String MSG_REGISTRADO = "Despacho registrado correctamente";
    private static final String MSG_ACTUALIZADO = "Despacho actualizado correctamente";
    private static final String MSG_NO_ENCONTRADO = "No se encontró el despacho";
    private static final String MSG_ACTIVADO = "Despacho activado correctamente";
    private static final String MSG_DESACTIVADO = "Despacho desactivado correctamente";
    private static final String MSG_REFERENCIA = "No se encontró una referencia asociada al despacho";

    private final DespachoRepository despachoRepository;
    private final ClienteRepository clienteRepository;
    private final NavieraRepository navieraRepository;
    private final DestinoRepository destinoRepository;
    private final OperadorLogisticoRepository operadorLogisticoRepository;
    private final PuertoLlegadaRepository puertoLlegadaRepository;
    private final ProductoRepository productoRepository;
    private final VariedadRepository variedadRepository;
    private final ViaRepository viaRepository;
    private final SituacionRepository situacionRepository;

    @Override
    @Transactional(readOnly = true)
    public CustomPage<DespachoResponse> listarDespachos(
            String texto,
            Integer clienteId,
            Integer productoId,
            Integer situacionId,
            Boolean activo,
            LocalDate fechaDesde,
            LocalDate fechaHasta,
            Pageable pageable
    ) {
        Page<DespachoResponse> page =
                despachoRepository
                        .listarDespachos(
                                texto,
                                clienteId,
                                productoId,
                                situacionId,
                                activo,
                                fechaDesde,
                                fechaHasta,
                                pageable
                        )
                        .map(DespachoResponse::from);

        return new CustomPage<>(page);
    }

    @Override
    @Transactional
    public ResponseEntity<Object> registrar(DespachoRegistroRequest request) {
        String codigo = request.getCodigo().trim();
        if (!referenciasExisten(request)) {
            return MessageResponse.setResponse(Boolean.FALSE, HttpStatus.NOT_FOUND, MSG_REFERENCIA);
        }

        if (request.getDespachoId() == 0) {
            if (despachoRepository.existsByCodigoIgnoreCase(codigo)) {
                return MessageResponse.setResponse(Boolean.FALSE, HttpStatus.CONFLICT, MSG_CODIGO);
            }
            DespachoEntity entity = new DespachoEntity();
            asignar(entity, request, codigo);
            entity.setActivo(Boolean.TRUE);
            despachoRepository.save(entity);
            return MessageResponse.setResponse(Boolean.TRUE, HttpStatus.CREATED, MSG_REGISTRADO);
        }

        DespachoEntity entity = despachoRepository.findById(request.getDespachoId()).orElse(null);
        if (entity == null) {
            return MessageResponse.setResponse(Boolean.FALSE, HttpStatus.NOT_FOUND, MSG_NO_ENCONTRADO);
        }
        if (despachoRepository.existsByCodigoIgnoreCaseAndDespachoIdNot(codigo, request.getDespachoId())) {
            return MessageResponse.setResponse(Boolean.FALSE, HttpStatus.CONFLICT, MSG_CODIGO);
        }
        asignar(entity, request, codigo);
        despachoRepository.save(entity);
        return MessageResponse.setResponse(Boolean.TRUE, HttpStatus.OK, MSG_ACTUALIZADO);
    }

    private void asignar(DespachoEntity entity, DespachoRegistroRequest request, String codigo) {
        entity.setCodigo(codigo);
        entity.setFechaDespacho(request.getFechaDespacho());
        entity.setFechaEstimadaLlegada(request.getFechaEstimadaLlegada());
        entity.setClienteId(request.getClienteId());
        entity.setNavieraId(request.getNavieraId());
        entity.setDestinoId(request.getDestinoId());
        entity.setOperadorLogisticoId(request.getOperadorLogisticoId());
        entity.setPuertoLlegadaId(request.getPuertoLlegadaId());
        entity.setProductoId(request.getProductoId());
        entity.setVariedadId(request.getVariedadId());
        entity.setViaId(request.getViaId());
        entity.setSituacionId(request.getSituacionId());
        entity.setCantidad(request.getCantidad());
        entity.setUnidadMedida(request.getUnidadMedida().trim());
        entity.setNumeroContenedor(request.getNumeroContenedor().trim());
        entity.setObservaciones(normalizar(request.getObservaciones()));
    }

    private boolean referenciasExisten(DespachoRegistroRequest request) {
        return clienteRepository.existsById(request.getClienteId())
                && navieraRepository.existsById(request.getNavieraId())
                && destinoRepository.existsById(request.getDestinoId())
                && operadorLogisticoRepository.existsById(request.getOperadorLogisticoId())
                && puertoLlegadaRepository.existsById(request.getPuertoLlegadaId())
                && productoRepository.existsById(request.getProductoId())
                && variedadRepository.existsById(request.getVariedadId())
                && viaRepository.existsById(request.getViaId())
                && situacionRepository.existsById(request.getSituacionId());
    }

    private String normalizar(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }

    @Override
    @Transactional
    public ResponseEntity<Object> cambiarEstado(Integer despachoId, Boolean activo) {
        DespachoEntity entity = despachoRepository.findById(despachoId).orElse(null);
        if (entity == null) {
            return MessageResponse.setResponse(Boolean.FALSE, HttpStatus.NOT_FOUND, MSG_NO_ENCONTRADO);
        }
        entity.setActivo(activo);
        despachoRepository.save(entity);
        String mensaje = Boolean.TRUE.equals(activo) ? MSG_ACTIVADO : MSG_DESACTIVADO;
        return MessageResponse.setResponse(Boolean.TRUE, HttpStatus.OK, mensaje);
    }
}
