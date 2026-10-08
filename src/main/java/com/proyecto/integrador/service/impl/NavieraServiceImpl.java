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

@Service @RequiredArgsConstructor
public class NavieraServiceImpl implements NavieraService {
    private static final String MSG_CODIGO = "El código ya está registrado";
    private static final String MSG_NOMBRE = "El nombre ya está registrado";
    private static final String MSG_REGISTRADA = "Naviera registrada correctamente";
    private static final String MSG_NO_ENCONTRADA = "No se encontró la naviera";
    private static final String MSG_ACTUALIZADA = "Naviera actualizada correctamente";
    private static final String MSG_ACTIVADA = "Naviera activada correctamente";
    private static final String MSG_DESACTIVADA = "Naviera desactivada correctamente";
    private final NavieraRepository navieraRepository;

    @Override @Transactional(readOnly = true)
    public CustomPage<NavieraResponse> listarNavieras(String texto, String pais, Boolean activo, Pageable pageable) {
        Page<NavieraResponse> page = navieraRepository.listarNavieras(texto, pais, activo, pageable).map(NavieraResponse::from);
        return new CustomPage<>(page);
    }

    @Override @Transactional
    public ResponseEntity<MessageResponse> registrar(NavieraRegistroRequest request) {
        String codigo = request.getCodigo().trim(), nombre = request.getNombre().trim(), pais = request.getPais().trim();
        if (request.getNavieraId() == 0) {
            if (navieraRepository.existsByCodigoIgnoreCase(codigo)) return MessageResponse.setResponse(Boolean.FALSE, HttpStatus.CONFLICT, MSG_CODIGO);
            if (navieraRepository.existsByNombreIgnoreCase(nombre)) return MessageResponse.setResponse(Boolean.FALSE, HttpStatus.CONFLICT, MSG_NOMBRE);
            NavieraEntity entity = new NavieraEntity(); asignar(entity, request, codigo, nombre, pais); entity.setActivo(Boolean.TRUE);
            navieraRepository.save(entity);
            return MessageResponse.setResponse(Boolean.TRUE, HttpStatus.CREATED, MSG_REGISTRADA);
        }
        NavieraEntity entity = navieraRepository.findById(request.getNavieraId()).orElse(null);
        if (entity == null) return MessageResponse.setResponse(Boolean.FALSE, HttpStatus.NOT_FOUND, MSG_NO_ENCONTRADA);
        if (navieraRepository.existsByCodigoIgnoreCaseAndNavieraIdNot(codigo, request.getNavieraId())) return MessageResponse.setResponse(Boolean.FALSE, HttpStatus.CONFLICT, MSG_CODIGO);
        if (navieraRepository.existsByNombreIgnoreCaseAndNavieraIdNot(nombre, request.getNavieraId())) return MessageResponse.setResponse(Boolean.FALSE, HttpStatus.CONFLICT, MSG_NOMBRE);
        asignar(entity, request, codigo, nombre, pais); navieraRepository.save(entity);
        return MessageResponse.setResponse(Boolean.TRUE, HttpStatus.OK, MSG_ACTUALIZADA);
    }

    private void asignar(NavieraEntity e, NavieraRegistroRequest r, String codigo, String nombre, String pais) {
        e.setCodigo(codigo); e.setNombre(nombre); e.setPais(pais); e.setContacto(normalizar(r.getContacto()));
        e.setCorreo(normalizar(r.getCorreo())); e.setTelefono(normalizar(r.getTelefono())); e.setSitioWeb(normalizar(r.getSitioWeb()));
    }
    private String normalizar(String value) { return value == null || value.isBlank() ? null : value.trim(); }

    @Override @Transactional
    public ResponseEntity<MessageResponse> cambiarEstado(Integer navieraId, Boolean activo) {
        NavieraEntity entity = navieraRepository.findById(navieraId).orElse(null);
        if (entity == null) return MessageResponse.setResponse(Boolean.FALSE, HttpStatus.NOT_FOUND, MSG_NO_ENCONTRADA);
        entity.setActivo(activo); navieraRepository.save(entity);
        return MessageResponse.setResponse(Boolean.TRUE, HttpStatus.OK, Boolean.TRUE.equals(activo) ? MSG_ACTIVADA : MSG_DESACTIVADA);
    }
}

