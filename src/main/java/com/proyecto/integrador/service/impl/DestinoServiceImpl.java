package com.proyecto.integrador.service.impl;

import com.proyecto.integrador.model.entity.DestinoEntity;
import com.proyecto.integrador.model.request.DestinoRegistroRequest;
import com.proyecto.integrador.model.response.DestinoResponse;
import com.proyecto.integrador.repository.DestinoRepository;
import com.proyecto.integrador.service.DestinoService;
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
public class DestinoServiceImpl implements DestinoService {

    private static final String MSG_DESTINO_YA_REGISTRADO = "El destino ya está registrado";
    private static final String MSG_DESTINO_REGISTRADO = "Destino registrado correctamente";
    private static final String MSG_DESTINO_NO_ENCONTRADO = "No se encontró el destino";
    private static final String MSG_DESTINO_ACTUALIZADO = "Destino actualizado correctamente";
    private static final String MSG_DESTINO_ACTIVADO = "Destino activado correctamente";
    private static final String MSG_DESTINO_DESACTIVADO = "Destino desactivado correctamente";

    private final DestinoRepository destinoRepository;

    @Override
    @Transactional(readOnly = true)
    public CustomPage<DestinoResponse> listarDestinos(
            String pais, String ciudad, Boolean activo, Pageable pageable) {
        Page<DestinoResponse> destinos = destinoRepository
                .listarDestinos(pais, ciudad, activo, pageable)
                .map(DestinoResponse::from);
        return new CustomPage<>(destinos);
    }

    @Override
    @Transactional
    public ResponseEntity<Object> registrar(DestinoRegistroRequest request) {
        String pais = request.getPais().trim();
        String ciudad = request.getCiudad().trim();

        if (request.getDestinoId() == 0) {
            if (destinoRepository.existsByPaisIgnoreCaseAndCiudadIgnoreCase(pais, ciudad)) {
                return MessageResponse.setResponse(
                        Boolean.FALSE, HttpStatus.CONFLICT, MSG_DESTINO_YA_REGISTRADO);
            }

            DestinoEntity entity = new DestinoEntity();
            entity.setPais(pais);
            entity.setCiudad(ciudad);
            entity.setActivo(Boolean.TRUE);
            destinoRepository.save(entity);

            return MessageResponse.setResponse(
                    Boolean.TRUE, HttpStatus.CREATED, MSG_DESTINO_REGISTRADO);
        }

        DestinoEntity entity = destinoRepository.findById(request.getDestinoId()).orElse(null);
        if (entity == null) {
            return MessageResponse.setResponse(
                    Boolean.FALSE, HttpStatus.NOT_FOUND, MSG_DESTINO_NO_ENCONTRADO);
        }

        if (destinoRepository.existsByPaisIgnoreCaseAndCiudadIgnoreCaseAndDestinoIdNot(
                pais, ciudad, request.getDestinoId())) {
            return MessageResponse.setResponse(
                    Boolean.FALSE, HttpStatus.CONFLICT, MSG_DESTINO_YA_REGISTRADO);
        }

        entity.setPais(pais);
        entity.setCiudad(ciudad);
        destinoRepository.save(entity);
        return MessageResponse.setResponse(
                Boolean.TRUE, HttpStatus.OK, MSG_DESTINO_ACTUALIZADO);
    }

    @Override
    @Transactional
    public ResponseEntity<Object> cambiarEstado(Integer destinoId, Boolean activo) {
        DestinoEntity entity = destinoRepository.findById(destinoId).orElse(null);
        if (entity == null) {
            return MessageResponse.setResponse(
                    Boolean.FALSE, HttpStatus.NOT_FOUND, MSG_DESTINO_NO_ENCONTRADO);
        }

        entity.setActivo(activo);
        destinoRepository.save(entity);

        String mensaje = Boolean.TRUE.equals(activo)
                ? MSG_DESTINO_ACTIVADO
                : MSG_DESTINO_DESACTIVADO;
        return MessageResponse.setResponse(Boolean.TRUE, HttpStatus.OK, mensaje);
    }
}
