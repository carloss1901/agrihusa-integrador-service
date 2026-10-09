package com.proyecto.integrador.service;

import com.proyecto.integrador.model.request.DespachoRegistroRequest;
import com.proyecto.integrador.model.response.DespachoResponse;
import com.proyecto.integrador.utils.CustomPage;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import java.time.LocalDate;

public interface DespachoService {
    CustomPage<DespachoResponse> listarDespachos(
            String texto,
            Integer clienteId,
            Integer productoId,
            Integer situacionId,
            Boolean activo,
            LocalDate fechaDesde,
            LocalDate fechaHasta,
            Pageable pageable
    );
    ResponseEntity<Object> registrar(DespachoRegistroRequest request);
    default ResponseEntity<Object> actualizar(DespachoRegistroRequest request) {
        return registrar(request);
    }
    ResponseEntity<Object> cambiarEstado(Integer despachoId, Boolean activo);
}
