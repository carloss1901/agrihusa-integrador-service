package com.proyecto.integrador.service.impl;

import com.proyecto.integrador.model.entity.ProductoEntity;
import com.proyecto.integrador.model.request.ProductoRegistroRequest;
import com.proyecto.integrador.model.response.ProductoResponse;
import com.proyecto.integrador.model.response.ComunResponse;
import com.proyecto.integrador.model.mapper.GlobalMapper;
import com.proyecto.integrador.repository.ProductoRepository;
import com.proyecto.integrador.service.ProductoService;
import com.proyecto.integrador.utils.CustomPage;
import com.proyecto.integrador.utils.MessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ProductoServiceImpl implements ProductoService {

    private static final String MSG_PRODUCTO_YA_REGISTRADO = "El producto ya está registrado";
    private static final String MSG_CODIGO_YA_REGISTRADO = "El código ya está registrado";
    private static final String MSG_NOMBRE_YA_REGISTRADO = "El nombre ya está registrado";
    private static final String MSG_PRODUCTO_REGISTRADO = "Producto registrado correctamente";
    private static final String MSG_PRODUCTO_NO_ENCONTRADO = "No se encontró el producto";
    private static final String MSG_PRODUCTO_ACTUALIZADO = "Producto actualizado correctamente";
    private static final String MSG_PRODUCTO_ACTIVADO = "Producto activado correctamente";
    private static final String MSG_PRODUCTO_DESACTIVADO = "Producto desactivado correctamente";

    private final ProductoRepository productoRepository;
    private final GlobalMapper globalMapper;

    public ProductoServiceImpl(ProductoRepository productoRepository) {
        this(productoRepository, new GlobalMapper());
    }

    @Override
    @Transactional(readOnly = true)
    public CustomPage<ProductoResponse> listarProductos(
            String texto, Boolean activo, Pageable pageable) {
        Page<ProductoResponse> productos = productoRepository
                .listarProductos(texto, activo, pageable)
                .map(projection -> globalMapper.map(projection, ProductoResponse.class));
        return new CustomPage<>(productos);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ComunResponse> listarProductosActivosCombo() {
        return productoRepository.findAllByActivoTrueOrderByNombreAsc()
                .stream()
                .map(producto -> new ComunResponse(producto.getProductoId(), producto.getNombre()))
                .toList();
    }

    @Override
    @Transactional
    public ResponseEntity<MessageResponse> registrar(ProductoRegistroRequest request) {
        String codigo = request.getCodigo().trim();
        String nombre = request.getNombre().trim();
        String descripcion = request.getDescripcion().trim();

        if (request.getProductoId() == 0) {
            if (productoRepository.existsByCodigoIgnoreCase(codigo)) {
                return MessageResponse.setResponse(Boolean.FALSE, HttpStatus.CONFLICT, MSG_CODIGO_YA_REGISTRADO);
            }
            if (productoRepository.existsByNombreIgnoreCase(nombre)) {
                return MessageResponse.setResponse(Boolean.FALSE, HttpStatus.CONFLICT, MSG_NOMBRE_YA_REGISTRADO);
            }

            ProductoEntity entity = new ProductoEntity();
            entity.setCodigo(codigo);
            entity.setNombre(nombre);
            entity.setDescripcion(descripcion);
            entity.setActivo(Boolean.TRUE);
            productoRepository.save(entity);

            return MessageResponse.setResponse(Boolean.TRUE, HttpStatus.CREATED, MSG_PRODUCTO_REGISTRADO);
        }

        ProductoEntity entity = productoRepository.findById(request.getProductoId()).orElse(null);
        if (entity == null) {
            return MessageResponse.setResponse(Boolean.FALSE, HttpStatus.NOT_FOUND, MSG_PRODUCTO_NO_ENCONTRADO);
        }

        if (productoRepository.existsByCodigoIgnoreCaseAndProductoIdNot(codigo, request.getProductoId())) {
            return MessageResponse.setResponse(Boolean.FALSE, HttpStatus.CONFLICT, MSG_CODIGO_YA_REGISTRADO);
        }
        if (productoRepository.existsByNombreIgnoreCaseAndProductoIdNot(nombre, request.getProductoId())) {
            return MessageResponse.setResponse(Boolean.FALSE, HttpStatus.CONFLICT, MSG_NOMBRE_YA_REGISTRADO);
        }

        entity.setCodigo(codigo);
        entity.setNombre(nombre);
        entity.setDescripcion(descripcion);
        productoRepository.save(entity);
        return MessageResponse.setResponse(Boolean.TRUE, HttpStatus.OK, MSG_PRODUCTO_ACTUALIZADO);
    }

    @Override
    @Transactional
    public ResponseEntity<MessageResponse> cambiarEstado(Integer productoId, Boolean activo) {
        ProductoEntity entity = productoRepository.findById(productoId).orElse(null);
        if (entity == null) {
            return MessageResponse.setResponse(Boolean.FALSE, HttpStatus.NOT_FOUND, MSG_PRODUCTO_NO_ENCONTRADO);
        }

        entity.setActivo(activo);
        productoRepository.save(entity);

        String mensaje = Boolean.TRUE.equals(activo) ? MSG_PRODUCTO_ACTIVADO : MSG_PRODUCTO_DESACTIVADO;
        return MessageResponse.setResponse(Boolean.TRUE, HttpStatus.OK, mensaje);
    }
}

