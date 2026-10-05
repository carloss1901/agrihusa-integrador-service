package com.proyecto.integrador.service.impl;

import com.proyecto.integrador.model.entity.NavieraEntity;
import com.proyecto.integrador.model.request.NavieraRegistroRequest;
import com.proyecto.integrador.model.response.NavieraResponse;
import com.proyecto.integrador.repository.NavieraRepository;
import com.proyecto.integrador.service.NavieraService;
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
public class NavieraServiceImpl implements NavieraService {
    private static final String MSG_CODIGO = "El código ya está registrado";
    private static final String MSG_NOMBRE = "El nombre ya está registrado";
    private static final String MSG_REGISTRADA = "Naviera registrada correctamente";
    private static final String MSG_NO_ENCONTRADA = "No se encontró la naviera";
    private static final String MSG_ENCONTRADA = "Naviera encontrada";
    private static final String MSG_ACTUALIZADA = "Naviera actualizada correctamente";
    private static final String MSG_ACTIVADA = "Naviera activada correctamente";
    private static final String MSG_DESACTIVADA = "Naviera desactivada correctamente";
    private static final String MSG_VALIDACION = "Validación de duplicados realizada";
    private final NavieraRepository navieraRepository;

    @Override @Transactional(readOnly = true)
    public CustomPage<NavieraResponse> listarNavieras(String texto, String pais, Boolean activo, Pageable pageable) {
        Page<NavieraResponse> page = navieraRepository.listarNavieras(texto, pais, activo, pageable).map(NavieraResponse::from);
        return new CustomPage<>(page);
    }

    @Override @Transactional(readOnly = true)
    public List<NavieraResponse> listarActivas() {
        return navieraRepository.findAllByActivoTrueOrderByNombreAsc().stream().map(NavieraResponse::from).toList();
    }

    @Override @Transactional(readOnly = true)
    public ResponseEntity<Object> obtenerPorId(Integer navieraId) {
        return navieraRepository.findById(navieraId)
                .map(e -> MessageResponse.setResponse(Boolean.TRUE, HttpStatus.OK, MSG_ENCONTRADA, NavieraResponse.from(e)))
                .orElseGet(() -> MessageResponse.setResponse(Boolean.FALSE, HttpStatus.NOT_FOUND, MSG_NO_ENCONTRADA));
    }

    @Override @Transactional(readOnly = true)
    public ResponseEntity<Object> validarDuplicados(String codigo, String nombre, Integer navieraId) {
        int id = navieraId == null ? 0 : navieraId;
        String cod = normalizarCodigo(codigo), nom = normalizarMayus(nombre);
        boolean existeCodigo = cod != null && (id == 0 ? navieraRepository.existsByCodigoIgnoreCase(cod)
                : navieraRepository.existsByCodigoIgnoreCaseAndNavieraIdNot(cod, id));
        boolean existeNombre = nom != null && (id == 0 ? navieraRepository.existsByNombreIgnoreCase(nom)
                : navieraRepository.existsByNombreIgnoreCaseAndNavieraIdNot(nom, id));
        return MessageResponse.setResponse(Boolean.TRUE, HttpStatus.OK, MSG_VALIDACION,
                Map.of("existeCodigo", existeCodigo, "existeNombre", existeNombre));
    }

    @Override @Transactional
    public ResponseEntity<Object> registrar(NavieraRegistroRequest request) {
        String codigo = normalizarCodigo(request.getCodigo()), nombre = normalizarMayus(request.getNombre()), pais = normalizarMayus(request.getPais());
        if (request.getNavieraId() == 0) {
            if (navieraRepository.existsByCodigoIgnoreCase(codigo)) return MessageResponse.setResponse(Boolean.FALSE, HttpStatus.CONFLICT, MSG_CODIGO);
            if (navieraRepository.existsByNombreIgnoreCase(nombre)) return MessageResponse.setResponse(Boolean.FALSE, HttpStatus.CONFLICT, MSG_NOMBRE);
            NavieraEntity entity = new NavieraEntity(); asignar(entity, request, codigo, nombre, pais); entity.setActivo(Boolean.TRUE);
            navieraRepository.save(entity);
            return MessageResponse.setResponse(Boolean.TRUE, HttpStatus.CREATED, MSG_REGISTRADA, NavieraResponse.from(entity));
        }
        NavieraEntity entity = navieraRepository.findById(request.getNavieraId()).orElse(null);
        if (entity == null) return MessageResponse.setResponse(Boolean.FALSE, HttpStatus.NOT_FOUND, MSG_NO_ENCONTRADA);
        if (navieraRepository.existsByCodigoIgnoreCaseAndNavieraIdNot(codigo, request.getNavieraId())) return MessageResponse.setResponse(Boolean.FALSE, HttpStatus.CONFLICT, MSG_CODIGO);
        if (navieraRepository.existsByNombreIgnoreCaseAndNavieraIdNot(nombre, request.getNavieraId())) return MessageResponse.setResponse(Boolean.FALSE, HttpStatus.CONFLICT, MSG_NOMBRE);
        asignar(entity, request, codigo, nombre, pais); navieraRepository.saveAndFlush(entity);
        return MessageResponse.setResponse(Boolean.TRUE, HttpStatus.OK, MSG_ACTUALIZADA, NavieraResponse.from(entity));
    }

    private void asignar(NavieraEntity e, NavieraRegistroRequest r, String codigo, String nombre, String pais) {
        e.setCodigo(codigo); e.setNombre(nombre); e.setPais(pais); e.setContacto(normalizar(r.getContacto()));
        String correo = normalizar(r.getCorreo()); e.setCorreo(correo == null ? null : correo.toLowerCase());
        e.setTelefono(normalizar(r.getTelefono())); e.setSitioWeb(normalizar(r.getSitioWeb()));
    }
    private String normalizar(String value) { return value == null || value.isBlank() ? null : value.trim(); }
    private String normalizarMayus(String value) { String v = normalizar(value); return v == null ? null : v.toUpperCase(); }
    private String normalizarCodigo(String value) { String v = normalizarMayus(value); return v == null ? null : v.replaceAll("\\s+", ""); }

    @Override @Transactional
    public ResponseEntity<Object> cambiarEstado(Integer navieraId, Boolean activo) {
        NavieraEntity entity = navieraRepository.findById(navieraId).orElse(null);
        if (entity == null) return MessageResponse.setResponse(Boolean.FALSE, HttpStatus.NOT_FOUND, MSG_NO_ENCONTRADA);
        entity.setActivo(activo); navieraRepository.save(entity);
        return MessageResponse.setResponse(Boolean.TRUE, HttpStatus.OK, Boolean.TRUE.equals(activo) ? MSG_ACTIVADA : MSG_DESACTIVADA);
    }
}
