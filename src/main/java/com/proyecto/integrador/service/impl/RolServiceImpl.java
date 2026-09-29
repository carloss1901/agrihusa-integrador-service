package com.proyecto.integrador.service.impl;

import com.proyecto.integrador.model.entity.RolEntity;
import com.proyecto.integrador.model.request.RolRegistroRequest;
import com.proyecto.integrador.model.response.RolResponse;
import com.proyecto.integrador.repository.RolRepository;
import com.proyecto.integrador.service.RolService;
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
public class RolServiceImpl implements RolService {

    private static final String MSG_ROL_YA_REGISTRADO = "El rol ya está registrado";
    private static final String MSG_ROL_REGISTRADO = "Rol registrado correctamente";
    private static final String MSG_ROL_NO_ENCONTRADO = "No se encontró el rol";
    private static final String MSG_ROL_ACTUALIZADO = "Rol actualizado correctamente";
    private static final String MSG_ROL_ACTIVADO = "Rol activado correctamente";
    private static final String MSG_ROL_DESACTIVADO = "Rol desactivado correctamente";

    private final RolRepository rolRepository;

    @Override
    @Transactional
    public ResponseEntity<Object> registrar(RolRegistroRequest request) {
        String nombre = request.getNombre().trim();
        String descripcion = request.getDescripcion().trim();

        if (request.getRolId() == 0) {
            if (rolRepository.existsByNombreIgnoreCase(nombre)) {
                return MessageResponse.setResponse(Boolean.FALSE, HttpStatus.CONFLICT, MSG_ROL_YA_REGISTRADO);
            }

            RolEntity entity = new RolEntity();
            entity.setNombre(nombre);
            entity.setDescripcion(descripcion);
            entity.setEsSistema(Boolean.FALSE);
            entity.setActivo(Boolean.TRUE);
            rolRepository.save(entity);

            return MessageResponse.setResponse(Boolean.TRUE, HttpStatus.CREATED, MSG_ROL_REGISTRADO);
        }

        RolEntity entity = rolRepository.findById(request.getRolId()).orElse(null);
        if (entity == null) {
            return MessageResponse.setResponse(Boolean.FALSE, HttpStatus.NOT_FOUND, MSG_ROL_NO_ENCONTRADO);
        }

        entity.setNombre(nombre);
        entity.setDescripcion(descripcion);
        rolRepository.save(entity);
        return MessageResponse.setResponse(Boolean.TRUE, HttpStatus.OK, MSG_ROL_ACTUALIZADO);
    }

    @Override
    @Transactional
    public ResponseEntity<Object> cambiarEstado(Integer rolId, Boolean activo) {
        RolEntity entity = rolRepository.findById(rolId).orElse(null);
        if (entity == null) {
            return MessageResponse.setResponse(Boolean.FALSE, HttpStatus.NOT_FOUND, MSG_ROL_NO_ENCONTRADO);
        }

        entity.setActivo(activo);
        rolRepository.save(entity);

        String mensaje = Boolean.TRUE.equals(activo) ? MSG_ROL_ACTIVADO : MSG_ROL_DESACTIVADO;
        return MessageResponse.setResponse(Boolean.TRUE, HttpStatus.OK, mensaje);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomPage<RolResponse> listarRoles(String nombre, Boolean activo, Pageable pageable) {
        Page<RolResponse> roles = rolRepository.listarRoles(nombre, activo, pageable).map(RolResponse::from);
        return new CustomPage<>(roles);
    }
}
