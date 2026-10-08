package com.proyecto.integrador.service.impl;

import com.proyecto.integrador.model.entity.ClienteEntity;
import com.proyecto.integrador.model.request.ClienteRegistroRequest;
import com.proyecto.integrador.model.response.ClienteResponse;
import com.proyecto.integrador.repository.ClienteRepository;
import com.proyecto.integrador.service.ClienteService;
import com.proyecto.integrador.utils.CustomPage;
import com.proyecto.integrador.utils.MessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteService {
    private static final String MSG_DOCUMENTO = "El número de documento ya está registrado";
    private static final String MSG_RAZON = "La razón social ya está registrada";
    private static final String MSG_REGISTRADO = "Cliente registrado correctamente";
    private static final String MSG_NO_ENCONTRADO = "No se encontró el cliente";
    private static final String MSG_ACTUALIZADO = "Cliente actualizado correctamente";
    private static final String MSG_ACTIVADO = "Cliente activado correctamente";
    private static final String MSG_DESACTIVADO = "Cliente desactivado correctamente";
    private final ClienteRepository clienteRepository;

    @Override @Transactional(readOnly = true)
    public CustomPage<ClienteResponse> listarClientes(String texto, String tipoDocumento, Boolean activo, Pageable pageable) {
        Page<ClienteResponse> page = clienteRepository.listarClientes(texto, tipoDocumento, activo, pageable).map(ClienteResponse::from);
        return new CustomPage<>(page);
    }

    @Override @Transactional
    public ResponseEntity<MessageResponse> registrar(ClienteRegistroRequest request) {
        String numero = request.getNumeroDocumento().trim(), razon = request.getRazonSocial().trim();
        if (request.getClienteId() == 0) {
            if (clienteRepository.existsByNumeroDocumentoIgnoreCase(numero)) return MessageResponse.setResponse(Boolean.FALSE, HttpStatus.CONFLICT, MSG_DOCUMENTO);
            if (clienteRepository.existsByRazonSocialIgnoreCase(razon)) return MessageResponse.setResponse(Boolean.FALSE, HttpStatus.CONFLICT, MSG_RAZON);
            ClienteEntity entity = new ClienteEntity(); asignar(entity, request, numero, razon); entity.setActivo(Boolean.TRUE);
            clienteRepository.save(entity);
            return MessageResponse.setResponse(Boolean.TRUE, HttpStatus.CREATED, MSG_REGISTRADO);
        }
        ClienteEntity entity = clienteRepository.findById(request.getClienteId()).orElse(null);
        if (entity == null) return MessageResponse.setResponse(Boolean.FALSE, HttpStatus.NOT_FOUND, MSG_NO_ENCONTRADO);
        if (clienteRepository.existsByNumeroDocumentoIgnoreCaseAndClienteIdNot(numero, request.getClienteId())) return MessageResponse.setResponse(Boolean.FALSE, HttpStatus.CONFLICT, MSG_DOCUMENTO);
        if (clienteRepository.existsByRazonSocialIgnoreCaseAndClienteIdNot(razon, request.getClienteId())) return MessageResponse.setResponse(Boolean.FALSE, HttpStatus.CONFLICT, MSG_RAZON);
        asignar(entity, request, numero, razon); clienteRepository.save(entity);
        return MessageResponse.setResponse(Boolean.TRUE, HttpStatus.OK, MSG_ACTUALIZADO);
    }

    private void asignar(ClienteEntity e, ClienteRegistroRequest r, String numero, String razon) {
        e.setTipoDocumento(r.getTipoDocumento().trim()); e.setNumeroDocumento(numero); e.setRazonSocial(razon);
        e.setNombreComercial(normalizar(r.getNombreComercial())); e.setContacto(normalizar(r.getContacto())); e.setCorreo(normalizar(r.getCorreo()));
        e.setTelefono(normalizar(r.getTelefono())); e.setDireccion(normalizar(r.getDireccion())); e.setPais(r.getPais().trim());
    }
    private String normalizar(String value) { return value == null || value.isBlank() ? null : value.trim(); }

    @Override @Transactional
    public ResponseEntity<MessageResponse> cambiarEstado(Integer clienteId, Boolean activo) {
        ClienteEntity entity = clienteRepository.findById(clienteId).orElse(null);
        if (entity == null) return MessageResponse.setResponse(Boolean.FALSE, HttpStatus.NOT_FOUND, MSG_NO_ENCONTRADO);
        entity.setActivo(activo); clienteRepository.save(entity);
        return MessageResponse.setResponse(Boolean.TRUE, HttpStatus.OK, Boolean.TRUE.equals(activo) ? MSG_ACTIVADO : MSG_DESACTIVADO);
    }
}

