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

import java.util.List;
import java.util.Map;

@Service @RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteService {
    private static final String MSG_DOCUMENTO = "El número de documento ya está registrado";
    private static final String MSG_RAZON = "La razón social ya está registrada";
    private static final String MSG_REGISTRADO = "Cliente registrado correctamente";
    private static final String MSG_NO_ENCONTRADO = "No se encontró el cliente";
    private static final String MSG_ENCONTRADO = "Cliente encontrado";
    private static final String MSG_ACTUALIZADO = "Cliente actualizado correctamente";
    private static final String MSG_ACTIVADO = "Cliente activado correctamente";
    private static final String MSG_DESACTIVADO = "Cliente desactivado correctamente";
    private static final String MSG_VALIDACION = "Validación de duplicados realizada";
    // Mismas reglas que el formulario del front (modal-cliente)
    private static final Map<String, String[]> FORMATOS_DOCUMENTO = Map.of(
            "RUC", new String[]{"^[0-9]{11}$", "El RUC debe contener exactamente 11 dígitos numéricos"},
            "DNI", new String[]{"^[0-9]{8}$", "El DNI debe contener exactamente 8 dígitos numéricos"},
            "CARNET_EXTRANJERIA", new String[]{"^[A-Z0-9]{9,12}$", "El carnet de extranjería debe tener entre 9 y 12 caracteres alfanuméricos"},
            "PASAPORTE", new String[]{"^[A-Z0-9]{6,12}$", "El pasaporte debe tener entre 6 y 12 caracteres alfanuméricos"},
            "OTRO", new String[]{"^[A-Z0-9-]{3,20}$", "El documento debe tener entre 3 y 20 caracteres alfanuméricos o guiones"});
    private final ClienteRepository clienteRepository;

    @Override @Transactional(readOnly = true)
    public CustomPage<ClienteResponse> listarClientes(String texto, String tipoDocumento, Boolean activo, Pageable pageable) {
        Page<ClienteResponse> page = clienteRepository.listarClientes(texto, tipoDocumento, activo, pageable).map(ClienteResponse::from);
        return new CustomPage<>(page);
    }

    @Override @Transactional(readOnly = true)
    public List<ClienteResponse> listarActivos() {
        return clienteRepository.findAllByActivoTrueOrderByRazonSocialAsc().stream().map(ClienteResponse::from).toList();
    }

    @Override @Transactional(readOnly = true)
    public ResponseEntity<Object> obtenerPorId(Integer clienteId) {
        return clienteRepository.findById(clienteId)
                .map(e -> MessageResponse.setResponse(Boolean.TRUE, HttpStatus.OK, MSG_ENCONTRADO, ClienteResponse.from(e)))
                .orElseGet(() -> MessageResponse.setResponse(Boolean.FALSE, HttpStatus.NOT_FOUND, MSG_NO_ENCONTRADO));
    }

    @Override @Transactional(readOnly = true)
    public ResponseEntity<Object> validarDuplicados(String numeroDocumento, String razonSocial, Integer clienteId) {
        int id = clienteId == null ? 0 : clienteId;
        String numero = normalizarDocumento(numeroDocumento), razon = normalizarMayus(razonSocial);
        boolean existeDocumento = numero != null && (id == 0 ? clienteRepository.existsByNumeroDocumentoIgnoreCase(numero)
                : clienteRepository.existsByNumeroDocumentoIgnoreCaseAndClienteIdNot(numero, id));
        boolean existeRazon = razon != null && (id == 0 ? clienteRepository.existsByRazonSocialIgnoreCase(razon)
                : clienteRepository.existsByRazonSocialIgnoreCaseAndClienteIdNot(razon, id));
        return MessageResponse.setResponse(Boolean.TRUE, HttpStatus.OK, MSG_VALIDACION,
                Map.of("existeNumeroDocumento", existeDocumento, "existeRazonSocial", existeRazon));
    }

    @Override @Transactional
    public ResponseEntity<Object> registrar(ClienteRegistroRequest request) {
        String numero = normalizarDocumento(request.getNumeroDocumento()), razon = normalizarMayus(request.getRazonSocial());
        String[] formato = FORMATOS_DOCUMENTO.get(request.getTipoDocumento().trim());
        if (formato != null && !numero.matches(formato[0])) return MessageResponse.setResponse(Boolean.FALSE, HttpStatus.BAD_REQUEST, formato[1]);
        if (request.getClienteId() == 0) {
            if (clienteRepository.existsByNumeroDocumentoIgnoreCase(numero)) return MessageResponse.setResponse(Boolean.FALSE, HttpStatus.CONFLICT, MSG_DOCUMENTO);
            if (clienteRepository.existsByRazonSocialIgnoreCase(razon)) return MessageResponse.setResponse(Boolean.FALSE, HttpStatus.CONFLICT, MSG_RAZON);
            ClienteEntity entity = new ClienteEntity(); asignar(entity, request, numero, razon); entity.setActivo(Boolean.TRUE);
            clienteRepository.save(entity);
            return MessageResponse.setResponse(Boolean.TRUE, HttpStatus.CREATED, MSG_REGISTRADO, ClienteResponse.from(entity));
        }
        ClienteEntity entity = clienteRepository.findById(request.getClienteId()).orElse(null);
        if (entity == null) return MessageResponse.setResponse(Boolean.FALSE, HttpStatus.NOT_FOUND, MSG_NO_ENCONTRADO);
        if (clienteRepository.existsByNumeroDocumentoIgnoreCaseAndClienteIdNot(numero, request.getClienteId())) return MessageResponse.setResponse(Boolean.FALSE, HttpStatus.CONFLICT, MSG_DOCUMENTO);
        if (clienteRepository.existsByRazonSocialIgnoreCaseAndClienteIdNot(razon, request.getClienteId())) return MessageResponse.setResponse(Boolean.FALSE, HttpStatus.CONFLICT, MSG_RAZON);
        asignar(entity, request, numero, razon); clienteRepository.saveAndFlush(entity);
        return MessageResponse.setResponse(Boolean.TRUE, HttpStatus.OK, MSG_ACTUALIZADO, ClienteResponse.from(entity));
    }

    private void asignar(ClienteEntity e, ClienteRegistroRequest r, String numero, String razon) {
        e.setTipoDocumento(r.getTipoDocumento().trim()); e.setNumeroDocumento(numero); e.setRazonSocial(razon);
        e.setNombreComercial(normalizarMayus(r.getNombreComercial())); e.setContacto(normalizar(r.getContacto()));
        String correo = normalizar(r.getCorreo()); e.setCorreo(correo == null ? null : correo.toLowerCase());
        e.setTelefono(normalizar(r.getTelefono())); e.setDireccion(normalizar(r.getDireccion())); e.setPais(normalizarMayus(r.getPais()));
    }
    private String normalizar(String value) { return value == null || value.isBlank() ? null : value.trim(); }
    private String normalizarMayus(String value) { String v = normalizar(value); return v == null ? null : v.toUpperCase(); }
    private String normalizarDocumento(String value) { String v = normalizarMayus(value); return v == null ? null : v.replaceAll("\\s+", ""); }

    @Override @Transactional
    public ResponseEntity<Object> cambiarEstado(Integer clienteId, Boolean activo) {
        ClienteEntity entity = clienteRepository.findById(clienteId).orElse(null);
        if (entity == null) return MessageResponse.setResponse(Boolean.FALSE, HttpStatus.NOT_FOUND, MSG_NO_ENCONTRADO);
        entity.setActivo(activo); clienteRepository.save(entity);
        return MessageResponse.setResponse(Boolean.TRUE, HttpStatus.OK, Boolean.TRUE.equals(activo) ? MSG_ACTIVADO : MSG_DESACTIVADO);
    }
}
