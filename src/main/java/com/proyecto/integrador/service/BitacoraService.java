package com.proyecto.integrador.service;

import com.proyecto.integrador.model.request.BitacoraRegistroRequest;
import com.proyecto.integrador.model.response.BitacoraResponse;
import com.proyecto.integrador.utils.CustomPage;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import java.time.LocalDateTime;

public interface BitacoraService {
    CustomPage<BitacoraResponse> listarBitacoras(
            String usuario,
            String modulo,
            String accion,
            String entidad,
            String resultado,
            Boolean activo,
            LocalDateTime fechaDesde,
            LocalDateTime fechaHasta,
            Pageable pageable
    );

    ResponseEntity<Object> registrar(BitacoraRegistroRequest request);
}
