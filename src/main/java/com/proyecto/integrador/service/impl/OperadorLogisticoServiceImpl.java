package com.proyecto.integrador.service.impl;

import com.proyecto.integrador.model.entity.OperadorLogisticoEntity;
import com.proyecto.integrador.model.request.OperadorLogisticoRegistroRequest;
import com.proyecto.integrador.model.response.OperadorLogisticoResponse;
import com.proyecto.integrador.repository.OperadorLogisticoRepository;
import com.proyecto.integrador.service.OperadorLogisticoService;
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
public class OperadorLogisticoServiceImpl implements OperadorLogisticoService {

    private static final String MSG_OPERADOR_YA_REGISTRADO = "El operador logístico ya está registrado";
    private static final String MSG_RUC_YA_REGISTRADO = "El RUC ya está registrado";
    private static final String MSG_RAZON_SOCIAL_YA_REGISTRADA = "La razón social ya está registrada";
    private static final String MSG_OPERADOR_REGISTRADO = "Operador logístico registrado correctamente";
    private static final String MSG_OPERADOR_NO_ENCONTRADO = "No se encontró el operador logístico";
    private static final String MSG_OPERADOR_ACTUALIZADO = "Operador logístico actualizado correctamente";
    private static final String MSG_OPERADOR_ACTIVADO = "Operador logístico activado correctamente";
    private static final String MSG_OPERADOR_DESACTIVADO = "Operador logístico desactivado correctamente";

    private final OperadorLogisticoRepository operadorRepository;

    @Override
    @Transactional(readOnly = true)
    public CustomPage<OperadorLogisticoResponse> listarOperadores(
            String texto, Boolean activo, Pageable pageable) {
        Page<OperadorLogisticoResponse> operadores = operadorRepository
                .listarOperadores(texto, activo, pageable)
                .map(OperadorLogisticoResponse::from);
        return new CustomPage<>(operadores);
    }

    @Override
    @Transactional
    public ResponseEntity<MessageResponse> registrar(OperadorLogisticoRegistroRequest request) {
        String ruc = request.getRuc().trim();
        String razonSocial = request.getRazonSocial().trim();

        if (request.getOperadorLogisticoId() == 0) {
            if (operadorRepository.existsByRucIgnoreCase(ruc)) {
                return MessageResponse.setResponse(Boolean.FALSE, HttpStatus.CONFLICT, MSG_RUC_YA_REGISTRADO);
            }
            if (operadorRepository.existsByRazonSocialIgnoreCase(razonSocial)) {
                return MessageResponse.setResponse(Boolean.FALSE, HttpStatus.CONFLICT, MSG_RAZON_SOCIAL_YA_REGISTRADA);
            }

            OperadorLogisticoEntity entity = new OperadorLogisticoEntity();
            asignarDatos(entity, request, ruc, razonSocial);
            entity.setActivo(Boolean.TRUE);
            operadorRepository.save(entity);

            return MessageResponse.setResponse(Boolean.TRUE, HttpStatus.CREATED, MSG_OPERADOR_REGISTRADO);
        }

        OperadorLogisticoEntity entity = operadorRepository.findById(request.getOperadorLogisticoId()).orElse(null);
        if (entity == null) {
            return MessageResponse.setResponse(Boolean.FALSE, HttpStatus.NOT_FOUND, MSG_OPERADOR_NO_ENCONTRADO);
        }

        if (operadorRepository.existsByRucIgnoreCaseAndOperadorLogisticoIdNot(
                ruc, request.getOperadorLogisticoId())) {
            return MessageResponse.setResponse(Boolean.FALSE, HttpStatus.CONFLICT, MSG_RUC_YA_REGISTRADO);
        }
        if (operadorRepository.existsByRazonSocialIgnoreCaseAndOperadorLogisticoIdNot(
                razonSocial, request.getOperadorLogisticoId())) {
            return MessageResponse.setResponse(Boolean.FALSE, HttpStatus.CONFLICT, MSG_RAZON_SOCIAL_YA_REGISTRADA);
        }

        asignarDatos(entity, request, ruc, razonSocial);
        operadorRepository.save(entity);
        return MessageResponse.setResponse(Boolean.TRUE, HttpStatus.OK, MSG_OPERADOR_ACTUALIZADO);
    }

    private void asignarDatos(
            OperadorLogisticoEntity entity,
            OperadorLogisticoRegistroRequest request,
            String ruc,
            String razonSocial) {
        entity.setRuc(ruc);
        entity.setRazonSocial(razonSocial);
        entity.setNombreComercial(normalizar(request.getNombreComercial()));
        entity.setContacto(normalizar(request.getContacto()));
        entity.setCorreo(normalizar(request.getCorreo()));
        entity.setTelefono(normalizar(request.getTelefono()));
        entity.setDireccion(normalizar(request.getDireccion()));
    }

    private String normalizar(String valor) {
        return valor == null || valor.isBlank() ? null : valor.trim();
    }

    @Override
    @Transactional
    public ResponseEntity<MessageResponse> cambiarEstado(Integer operadorLogisticoId, Boolean activo) {
        OperadorLogisticoEntity entity = operadorRepository
                .findById(operadorLogisticoId).orElse(null);
        if (entity == null) {
            return MessageResponse.setResponse(Boolean.FALSE, HttpStatus.NOT_FOUND, MSG_OPERADOR_NO_ENCONTRADO);
        }

        entity.setActivo(activo);
        operadorRepository.save(entity);

        String mensaje = Boolean.TRUE.equals(activo) ? MSG_OPERADOR_ACTIVADO : MSG_OPERADOR_DESACTIVADO;
        return MessageResponse.setResponse(Boolean.TRUE, HttpStatus.OK, mensaje);
    }
}

