package com.proyecto.integrador.service.impl;

import com.proyecto.integrador.model.entity.PuertoLlegadaEntity;
import com.proyecto.integrador.model.request.PuertoLlegadaRegistroRequest;
import com.proyecto.integrador.model.response.PuertoLlegadaResponse;
import com.proyecto.integrador.repository.PuertoLlegadaRepository;
import com.proyecto.integrador.service.PuertoLlegadaService;
import com.proyecto.integrador.utils.CustomPage;
import com.proyecto.integrador.utils.MessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PuertoLlegadaServiceImpl implements PuertoLlegadaService {

    private static final String MSG_PUERTO_YA_REGISTRADO = "El código ya está registrado";
    private static final String MSG_PUERTO_PAIS_YA_REGISTRADO = "El puerto ya está registrado para el país indicado";
    private static final String MSG_PUERTO_REGISTRADO = "Puerto de llegada registrado correctamente";
    private static final String MSG_PUERTO_NO_ENCONTRADO = "No se encontró el puerto de llegada";
    private static final String MSG_PUERTO_ACTUALIZADO = "Puerto de llegada actualizado correctamente";
    private static final String MSG_PUERTO_ACTIVADO = "Puerto de llegada activado correctamente";
    private static final String MSG_PUERTO_DESACTIVADO = "Puerto de llegada desactivado correctamente";

    private final PuertoLlegadaRepository puertoRepository;

    @Override
    @Transactional(readOnly = true)
    public CustomPage<PuertoLlegadaResponse> listarPuertos(
            String texto, String pais, Boolean activo, Pageable pageable) {
        Page<PuertoLlegadaResponse> puertos = puertoRepository
                .listarPuertos(texto, pais, activo, pageable)
                .map(PuertoLlegadaResponse::from);
        return new CustomPage<>(puertos);
    }

    @Override
    @Transactional
    public ResponseEntity<Object> registrar(PuertoLlegadaRegistroRequest request) {
        String codigo = request.getCodigo().trim();
        String puerto = request.getPuerto().trim();
        String pais = request.getPais().trim();

        if (request.getPuertoLlegadaId() == 0) {
            if (puertoRepository.existsByCodigoIgnoreCase(codigo)) {
                return MessageResponse.setResponse(Boolean.FALSE, HttpStatus.CONFLICT, MSG_PUERTO_YA_REGISTRADO);
            }
            if (puertoRepository.existsByPuertoIgnoreCaseAndPaisIgnoreCase(puerto, pais)) {
                return MessageResponse.setResponse(Boolean.FALSE, HttpStatus.CONFLICT, MSG_PUERTO_PAIS_YA_REGISTRADO);
            }

            PuertoLlegadaEntity entity = new PuertoLlegadaEntity();
            entity.setCodigo(codigo);
            entity.setPuerto(puerto);
            entity.setPais(pais);
            entity.setActivo(Boolean.TRUE);
            puertoRepository.save(entity);

            return MessageResponse.setResponse(Boolean.TRUE, HttpStatus.CREATED, MSG_PUERTO_REGISTRADO);
        }

        PuertoLlegadaEntity entity = puertoRepository.findById(request.getPuertoLlegadaId()).orElse(null);
        if (entity == null) {
            return MessageResponse.setResponse(Boolean.FALSE, HttpStatus.NOT_FOUND, MSG_PUERTO_NO_ENCONTRADO);
        }

        if (puertoRepository.existsByCodigoIgnoreCaseAndPuertoLlegadaIdNot(
                codigo, request.getPuertoLlegadaId())) {
            return MessageResponse.setResponse(Boolean.FALSE, HttpStatus.CONFLICT, MSG_PUERTO_YA_REGISTRADO);
        }
        if (puertoRepository.existsByPuertoIgnoreCaseAndPaisIgnoreCaseAndPuertoLlegadaIdNot(
                puerto, pais, request.getPuertoLlegadaId())) {
            return MessageResponse.setResponse(Boolean.FALSE, HttpStatus.CONFLICT, MSG_PUERTO_PAIS_YA_REGISTRADO);
        }

        entity.setCodigo(codigo);
        entity.setPuerto(puerto);
        entity.setPais(pais);
        puertoRepository.save(entity);
        return MessageResponse.setResponse(Boolean.TRUE, HttpStatus.OK, MSG_PUERTO_ACTUALIZADO);
    }

    @Override
    @Transactional
    public ResponseEntity<Object> cambiarEstado(Integer puertoLlegadaId, Boolean activo) {
        PuertoLlegadaEntity entity = puertoRepository.findById(puertoLlegadaId).orElse(null);
        if (entity == null) {
            return MessageResponse.setResponse(Boolean.FALSE, HttpStatus.NOT_FOUND, MSG_PUERTO_NO_ENCONTRADO);
        }

        entity.setActivo(activo);
        puertoRepository.save(entity);

        String mensaje = Boolean.TRUE.equals(activo) ? MSG_PUERTO_ACTIVADO : MSG_PUERTO_DESACTIVADO;
        return MessageResponse.setResponse(Boolean.TRUE, HttpStatus.OK, mensaje);
    }
}
