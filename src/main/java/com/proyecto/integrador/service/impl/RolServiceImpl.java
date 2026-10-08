package com.proyecto.integrador.service.impl;

import com.proyecto.integrador.model.entity.RolEntity;
import com.proyecto.integrador.model.entity.PermisoEntity;
import com.proyecto.integrador.model.entity.ModuloEntity;
import com.proyecto.integrador.model.entity.RolPermisoEntity;
import com.proyecto.integrador.model.request.RolRegistroRequest;
import com.proyecto.integrador.model.request.RolPermisoRequest;
import com.proyecto.integrador.model.response.RolResponse;
import com.proyecto.integrador.repository.PermisoRepository;
import com.proyecto.integrador.repository.ModuloRepository;
import com.proyecto.integrador.repository.RolRepository;
import com.proyecto.integrador.repository.RolPermisoRepository;
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RolServiceImpl implements RolService {

    private static final String MSG_ROL_YA_REGISTRADO = "El rol ya está registrado";
    private static final String MSG_ROL_REGISTRADO = "Rol registrado correctamente";
    private static final String MSG_ROL_NO_ENCONTRADO = "No se encontró el rol";
    private static final String MSG_ROL_ACTUALIZADO = "Rol actualizado correctamente";
    private static final String MSG_ROL_ACTIVADO = "Rol activado correctamente";
    private static final String MSG_ROL_DESACTIVADO = "Rol desactivado correctamente";
    private static final String MSG_MODULO_NO_ENCONTRADO = "No se encontró el módulo";
    private static final String MSG_PERMISO_NO_ENCONTRADO = "No se encontró el permiso";

    private final RolRepository rolRepository;
    private final ModuloRepository moduloRepository;
    private final PermisoRepository permisoRepository;
    private final RolPermisoRepository rolPermisoRepository;

    @Override
    @Transactional(readOnly = true)
    public CustomPage<RolResponse> listarRoles(String nombre, Boolean activo, Pageable pageable) {
        Page<RolResponse> roles = rolRepository.listarRoles(nombre, activo, pageable).map(RolResponse::from);
        return new CustomPage<>(roles);
    }

    @Override
    @Transactional
    public ResponseEntity<MessageResponse> cambiarEstado(Integer rolId, Boolean activo) {
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
    @Transactional
    public ResponseEntity<MessageResponse> registrar(RolRegistroRequest request) {
        String nombre = request.getNombre().trim();
        String descripcion = request.getDescripcion().trim();
        List<PermisoSeleccionado> permisos = new ArrayList<>();
        Set<String> permisosProcesados = new HashSet<>();

        for (RolPermisoRequest permisoRequest : request.getPermisos()) {
            String modulo = permisoRequest.getModulo().trim().toLowerCase(Locale.ROOT);
            ModuloEntity moduloEntity = moduloRepository.findByCodigoAndActivoTrue(modulo).orElse(null);

            if (moduloEntity == null) {
                return MessageResponse.setResponse(Boolean.FALSE, HttpStatus.BAD_REQUEST, MSG_MODULO_NO_ENCONTRADO);
            }

            for (String accionRequest : permisoRequest.getAcciones()) {
                String accion = accionRequest.trim().toLowerCase(Locale.ROOT);
                String clave = modulo + ":" + accion;
                if (!permisosProcesados.add(clave)) {
                    continue;
                }

                PermisoEntity permiso = permisoRepository.findByAccionAndActivoTrue(accion).orElse(null);

                if (permiso == null) {
                    return MessageResponse.setResponse(Boolean.FALSE, HttpStatus.BAD_REQUEST, MSG_PERMISO_NO_ENCONTRADO);
                }
                permisos.add(new PermisoSeleccionado(moduloEntity.getModuloId(), permiso));
            }
        }

        RolEntity entity;
        if (request.getRolId() == 0) {
            if (rolRepository.existsByNombreIgnoreCase(nombre)) {
                return MessageResponse.setResponse(Boolean.FALSE, HttpStatus.CONFLICT, MSG_ROL_YA_REGISTRADO);
            }

            entity = new RolEntity();
            entity.setNombre(nombre);
            entity.setDescripcion(descripcion);
            entity.setEsSistema(Boolean.FALSE);
            entity.setActivo(Boolean.TRUE);
        } else {
            entity = rolRepository.findById(request.getRolId()).orElse(null);
            if (entity == null) {
                return MessageResponse.setResponse(Boolean.FALSE, HttpStatus.NOT_FOUND, MSG_ROL_NO_ENCONTRADO);
            }

            entity.setNombre(nombre);
            entity.setDescripcion(descripcion);
        }

        rolRepository.save(entity);
        sincronizarPermisos(entity.getRolId(), permisos);

        HttpStatus status = request.getRolId() == 0 ? HttpStatus.CREATED : HttpStatus.OK;
        String mensaje = request.getRolId() == 0 ? MSG_ROL_REGISTRADO : MSG_ROL_ACTUALIZADO;
        return MessageResponse.setResponse(Boolean.TRUE, status, mensaje);
    }

    private void sincronizarPermisos(Integer rolId, List<PermisoSeleccionado> seleccionados) {
        Set<String> clavesSeleccionadas = seleccionados.stream()
                .map(permiso -> construirClave(permiso.moduloId(), permiso.permiso().getPermisoId()))
                .collect(Collectors.toSet());

        List<RolPermisoEntity> relaciones = rolPermisoRepository.findAllByRolId(rolId);
        Set<String> clavesExistentes = new HashSet<>();
        Map<String, RolPermisoEntity> relacionesPorClave = new HashMap<>();

        for (RolPermisoEntity relacion : relaciones) {
            String clave = construirClave(relacion.getModuloId(), relacion.getPermisoId());
            clavesExistentes.add(clave);
            relacionesPorClave.put(clave, relacion);
            relacion.setActivo(clavesSeleccionadas.contains(clave) ? Boolean.TRUE : Boolean.FALSE);
        }

        for (PermisoSeleccionado seleccionado : seleccionados) {
            Integer moduloId = seleccionado.moduloId();
            Integer permisoId = seleccionado.permiso().getPermisoId();
            String clave = construirClave(moduloId, permisoId);

            RolPermisoEntity relacion = relacionesPorClave.get(clave);
            if (relacion == null) {
                relacion = rolPermisoRepository
                        .findByRolIdAndModuloIdAndPermisoId(rolId, moduloId, permisoId)
                        .orElse(null);
            }

            if (relacion == null) {
                relaciones.add(crearRelacion(rolId, moduloId, permisoId));
            } else {
                relacion.setActivo(Boolean.TRUE);
                if (!clavesExistentes.contains(clave)) {
                    relaciones.add(relacion);
                }
            }
        }

        rolPermisoRepository.saveAll(relaciones);
    }

    private String construirClave(Integer moduloId, Integer permisoId) {
        return moduloId + ":" + permisoId;
    }

    private RolPermisoEntity crearRelacion(Integer rolId, Integer moduloId, Integer permisoId) {
        RolPermisoEntity relacion = new RolPermisoEntity();
        relacion.setRolId(rolId);
        relacion.setModuloId(moduloId);
        relacion.setPermisoId(permisoId);
        relacion.setActivo(Boolean.TRUE);
        return relacion;
    }

    private record PermisoSeleccionado(Integer moduloId, PermisoEntity permiso) {}
}

