package com.proyecto.integrador.service.impl;

import com.proyecto.integrador.model.entity.ProductoEntity;
import com.proyecto.integrador.model.entity.VariedadEntity;
import com.proyecto.integrador.model.request.VariedadRegistroRequest;
import com.proyecto.integrador.model.response.VariedadResponse;
import com.proyecto.integrador.repository.ProductoRepository;
import com.proyecto.integrador.repository.VariedadRepository;
import com.proyecto.integrador.service.VariedadService;
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
public class VariedadServiceImpl implements VariedadService {

    private static final String MSG_VARIEDAD_YA_REGISTRADA = "La variedad ya está registrada";
    private static final String MSG_VARIEDAD_REGISTRADA = "Variedad registrada correctamente";
    private static final String MSG_VARIEDAD_NO_ENCONTRADA = "No se encontró la variedad";
    private static final String MSG_VARIEDAD_ACTUALIZADA = "Variedad actualizada correctamente";
    private static final String MSG_VARIEDAD_ACTIVADA = "Variedad activada correctamente";
    private static final String MSG_VARIEDAD_DESACTIVADA = "Variedad desactivada correctamente";
    private static final String MSG_PRODUCTO_NO_ENCONTRADO = "No se encontró el producto";
    private static final String MSG_PRODUCTO_INACTIVO = "No se puede activar la variedad porque el producto está inactivo";

    private final VariedadRepository variedadRepository;
    private final ProductoRepository productoRepository;

    @Override
    @Transactional(readOnly = true)
    public CustomPage<VariedadResponse> listarVariedades(
            String texto, Integer productoId, Boolean activo, Pageable pageable) {
        Page<VariedadResponse> variedades = variedadRepository
                .listarVariedades(texto, productoId, activo, pageable)
                .map(VariedadResponse::from);
        return new CustomPage<>(variedades);
    }

    @Override
    @Transactional
    public ResponseEntity<Object> registrar(VariedadRegistroRequest request) {
        String nombre = request.getNombre().trim();
        ProductoEntity producto = productoRepository.findById(request.getProductoId()).orElse(null);
        if (producto == null) {
            return MessageResponse.setResponse(
                    Boolean.FALSE, HttpStatus.NOT_FOUND, MSG_PRODUCTO_NO_ENCONTRADO);
        }

        if (request.getVariedadId() == 0) {
            if (variedadRepository.existsByProductoIdAndNombreIgnoreCase(
                    request.getProductoId(), nombre)) {
                return MessageResponse.setResponse(
                        Boolean.FALSE, HttpStatus.CONFLICT, MSG_VARIEDAD_YA_REGISTRADA);
            }

            VariedadEntity entity = new VariedadEntity();
            entity.setProductoId(request.getProductoId());
            entity.setNombre(nombre);
            entity.setActivo(Boolean.TRUE);
            variedadRepository.save(entity);

            return MessageResponse.setResponse(
                    Boolean.TRUE, HttpStatus.CREATED, MSG_VARIEDAD_REGISTRADA);
        }

        VariedadEntity entity = variedadRepository.findById(request.getVariedadId()).orElse(null);
        if (entity == null) {
            return MessageResponse.setResponse(
                    Boolean.FALSE, HttpStatus.NOT_FOUND, MSG_VARIEDAD_NO_ENCONTRADA);
        }

        if (variedadRepository.existsByProductoIdAndNombreIgnoreCaseAndVariedadIdNot(
                request.getProductoId(), nombre, request.getVariedadId())) {
            return MessageResponse.setResponse(
                    Boolean.FALSE, HttpStatus.CONFLICT, MSG_VARIEDAD_YA_REGISTRADA);
        }

        entity.setProductoId(request.getProductoId());
        entity.setNombre(nombre);
        variedadRepository.save(entity);
        return MessageResponse.setResponse(
                Boolean.TRUE, HttpStatus.OK, MSG_VARIEDAD_ACTUALIZADA);
    }

    @Override
    @Transactional
    public ResponseEntity<Object> cambiarEstado(Integer variedadId, Boolean activo) {
        VariedadEntity entity = variedadRepository.findById(variedadId).orElse(null);
        if (entity == null) {
            return MessageResponse.setResponse(
                    Boolean.FALSE, HttpStatus.NOT_FOUND, MSG_VARIEDAD_NO_ENCONTRADA);
        }

        if (Boolean.TRUE.equals(activo)) {
            ProductoEntity producto = productoRepository.findById(entity.getProductoId()).orElse(null);
            if (producto == null) {
                return MessageResponse.setResponse(
                        Boolean.FALSE, HttpStatus.NOT_FOUND, MSG_PRODUCTO_NO_ENCONTRADO);
            }
            if (!Boolean.TRUE.equals(producto.getActivo())) {
                return MessageResponse.setResponse(
                        Boolean.FALSE, HttpStatus.BAD_REQUEST, MSG_PRODUCTO_INACTIVO);
            }
        }

        entity.setActivo(activo);
        variedadRepository.save(entity);

        String mensaje = Boolean.TRUE.equals(activo)
                ? MSG_VARIEDAD_ACTIVADA
                : MSG_VARIEDAD_DESACTIVADA;
        return MessageResponse.setResponse(Boolean.TRUE, HttpStatus.OK, mensaje);
    }
}
