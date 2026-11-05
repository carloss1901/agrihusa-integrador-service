package com.proyecto.integrador.service;

import com.proyecto.integrador.utils.MessageResponse;

import com.proyecto.integrador.model.request.BitacoraRegistroRequest;
import com.proyecto.integrador.model.response.BitacoraResponse;
import com.proyecto.integrador.utils.CustomPage;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

public interface BitacoraService {
    CustomPage<BitacoraResponse> listarBitacoras(String usuario, String modulo, String accion,
                                                  String entidad, String resultado, Boolean activo,
                                                  LocalDate fechaDesde, LocalDate fechaHasta,
                                                  Pageable pageable);

    ResponseEntity<MessageResponse> registrar(BitacoraRegistroRequest request);
}

