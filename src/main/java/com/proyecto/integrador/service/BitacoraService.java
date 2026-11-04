package com.proyecto.integrador.service;

import com.proyecto.integrador.utils.MessageResponse;

import com.proyecto.integrador.model.request.BitacoraRegistroRequest;
import com.proyecto.integrador.model.response.BitacoraResponse;
import com.proyecto.integrador.utils.CustomPage;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface BitacoraService {
    CustomPage<BitacoraResponse> listarBitacoras(Integer usuarioId, String modulo, String accion,
                                                  String entidad, String resultado, Boolean activo,
                                                  Pageable pageable);

    ResponseEntity<MessageResponse> registrar(BitacoraRegistroRequest request);
}

