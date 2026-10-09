package com.proyecto.integrador.service.impl;

import com.proyecto.integrador.model.entity.SituacionEntity;
import com.proyecto.integrador.model.request.SituacionRegistroRequest;
import com.proyecto.integrador.model.response.SituacionResponse;
import com.proyecto.integrador.repository.SituacionRepository;
import com.proyecto.integrador.service.SituacionService;
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
public class SituacionServiceImpl implements SituacionService {

    private static final String MSG_SITUACION_YA_REGISTRADA = "La situación ya está registrada";
    private static final String MSG_SITUACION_REGISTRADA = "Situación registrada correctamente";
    private static final String MSG_SITUACION_NO_ENCONTRADA = "No se encontró la situación";
    private static final String MSG_SITUACION_ACTUALIZADA = "Situación actualizada correctamente";
    private static final String MSG_SITUACION_ACTIVADA = "Situación activada correctamente";
    private static final String MSG_SITUACION_DESACTIVADA = "Situación desactivada correctamente";

    private final SituacionRepository situacionRepository;

    @Override
    @Transactional(readOnly = true)
    public CustomPage<SituacionResponse> listarSituaciones(
            String descripcion, Boolean activo, Pageable pageable) {
        Page<SituacionResponse> situaciones = situacionRepository
                .listarSituaciones(descripcion, activo, pageable)
                .map(SituacionResponse::from);
        return new CustomPage<>(situaciones);
    }

    @Override
    @Transactional
    public ResponseEntity<Object> registrar(SituacionRegistroRequest request) {
        String descripcion = request.getDescripcion().trim();

        if (request.getSituacionId() == 0) {
            if (situacionRepository.existsByDescripcionIgnoreCase(descripcion)) {
                return MessageResponse.setResponse(Boolean.FALSE, HttpStatus.CONFLICT, MSG_SITUACION_YA_REGISTRADA);
            }

            SituacionEntity entity = new SituacionEntity();
            entity.setDescripcion(descripcion);
            entity.setActivo(Boolean.TRUE);
            situacionRepository.save(entity);

            return MessageResponse.setResponse(Boolean.TRUE, HttpStatus.CREATED, MSG_SITUACION_REGISTRADA);
        }

        SituacionEntity entity = situacionRepository.findById(request.getSituacionId()).orElse(null);
        if (entity == null) {
            return MessageResponse.setResponse(Boolean.FALSE, HttpStatus.NOT_FOUND, MSG_SITUACION_NO_ENCONTRADA);
        }

        if (situacionRepository.existsByDescripcionIgnoreCaseAndSituacionIdNot(
                descripcion, request.getSituacionId())) {
            return MessageResponse.setResponse(Boolean.FALSE, HttpStatus.CONFLICT, MSG_SITUACION_YA_REGISTRADA);
        }

        entity.setDescripcion(descripcion);
        situacionRepository.save(entity);
        return MessageResponse.setResponse(Boolean.TRUE, HttpStatus.OK, MSG_SITUACION_ACTUALIZADA);
    }

    @Override
    @Transactional
    public ResponseEntity<Object> cambiarEstado(Integer situacionId, Boolean activo) {
        SituacionEntity entity = situacionRepository.findById(situacionId).orElse(null);
        if (entity == null) {
            return MessageResponse.setResponse(Boolean.FALSE, HttpStatus.NOT_FOUND, MSG_SITUACION_NO_ENCONTRADA);
        }

        entity.setActivo(activo);
        situacionRepository.save(entity);

        String mensaje = Boolean.TRUE.equals(activo) ? MSG_SITUACION_ACTIVADA : MSG_SITUACION_DESACTIVADA;
        return MessageResponse.setResponse(Boolean.TRUE, HttpStatus.OK, mensaje);
    }
}
