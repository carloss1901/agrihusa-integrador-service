package com.proyecto.integrador.service.impl;

import com.proyecto.integrador.model.entity.ViaEntity;
import com.proyecto.integrador.model.request.ViaRegistroRequest;
import com.proyecto.integrador.model.response.ViaResponse;
import com.proyecto.integrador.model.mapper.GlobalMapper;
import com.proyecto.integrador.repository.ViaRepository;
import com.proyecto.integrador.service.ViaService;
import com.proyecto.integrador.utils.CustomPage;
import com.proyecto.integrador.utils.MessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ViaServiceImpl implements ViaService {

    private static final String MSG_VIA_YA_REGISTRADA = "La vía ya está registrada";
    private static final String MSG_VIA_REGISTRADA = "Vía registrada correctamente";
    private static final String MSG_VIA_NO_ENCONTRADA = "No se encontró la vía";
    private static final String MSG_VIA_ACTUALIZADA = "Vía actualizada correctamente";
    private static final String MSG_VIA_ACTIVADA = "Vía activada correctamente";
    private static final String MSG_VIA_DESACTIVADA = "Vía desactivada correctamente";

    private final ViaRepository viaRepository;
    private final GlobalMapper globalMapper;

    public ViaServiceImpl(ViaRepository viaRepository) {
        this(viaRepository, new GlobalMapper());
    }

    @Override
    @Transactional(readOnly = true)
    public CustomPage<ViaResponse> listarVias(
            String descripcion, Boolean activo, Pageable pageable) {
        Page<ViaResponse> vias = viaRepository
                .listarVias(descripcion, activo, pageable)
                .map(projection -> globalMapper.map(projection, ViaResponse.class));
        return new CustomPage<>(vias);
    }

    @Override
    @Transactional
    public ResponseEntity<MessageResponse> registrar(ViaRegistroRequest request) {
        String descripcion = request.getDescripcion().trim();

        if (request.getViaId() == 0) {
            if (viaRepository.existsByDescripcionIgnoreCase(descripcion)) {
                return MessageResponse.setResponse(Boolean.FALSE, HttpStatus.CONFLICT, MSG_VIA_YA_REGISTRADA);
            }

            ViaEntity entity = new ViaEntity();
            entity.setDescripcion(descripcion);
            entity.setActivo(Boolean.TRUE);
            viaRepository.save(entity);

            return MessageResponse.setResponse(Boolean.TRUE, HttpStatus.CREATED, MSG_VIA_REGISTRADA);
        }

        ViaEntity entity = viaRepository.findById(request.getViaId()).orElse(null);
        if (entity == null) {
            return MessageResponse.setResponse(Boolean.FALSE, HttpStatus.NOT_FOUND, MSG_VIA_NO_ENCONTRADA);
        }

        if (viaRepository.existsByDescripcionIgnoreCaseAndViaIdNot(descripcion, request.getViaId())) {
            return MessageResponse.setResponse(Boolean.FALSE, HttpStatus.CONFLICT, MSG_VIA_YA_REGISTRADA);
        }

        entity.setDescripcion(descripcion);
        viaRepository.save(entity);
        return MessageResponse.setResponse(Boolean.TRUE, HttpStatus.OK, MSG_VIA_ACTUALIZADA);
    }

    @Override
    @Transactional
    public ResponseEntity<MessageResponse> cambiarEstado(Integer viaId, Boolean activo) {
        ViaEntity entity = viaRepository.findById(viaId).orElse(null);
        if (entity == null) {
            return MessageResponse.setResponse(Boolean.FALSE, HttpStatus.NOT_FOUND, MSG_VIA_NO_ENCONTRADA);
        }

        entity.setActivo(activo);
        viaRepository.save(entity);

        String mensaje = Boolean.TRUE.equals(activo) ? MSG_VIA_ACTIVADA : MSG_VIA_DESACTIVADA;
        return MessageResponse.setResponse(Boolean.TRUE, HttpStatus.OK, mensaje);
    }
}

