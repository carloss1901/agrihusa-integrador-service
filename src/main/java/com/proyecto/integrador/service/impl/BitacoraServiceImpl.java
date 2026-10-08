package com.proyecto.integrador.service.impl;

import com.proyecto.integrador.model.entity.BitacoraEntity;
import com.proyecto.integrador.model.request.BitacoraRegistroRequest;
import com.proyecto.integrador.model.response.BitacoraResponse;
import com.proyecto.integrador.model.mapper.GlobalMapper;
import com.proyecto.integrador.repository.BitacoraRepository;
import com.proyecto.integrador.service.BitacoraService;
import com.proyecto.integrador.utils.CustomPage;
import com.proyecto.integrador.utils.MessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BitacoraServiceImpl implements BitacoraService {

    private static final String MSG_REGISTRADA = "Bitácora registrada correctamente";
    private final BitacoraRepository bitacoraRepository;
    private final GlobalMapper globalMapper;

    public BitacoraServiceImpl(BitacoraRepository bitacoraRepository) {
        this(bitacoraRepository, new GlobalMapper());
    }

    @Override
    @Transactional(readOnly = true)
    public CustomPage<BitacoraResponse> listarBitacoras(Integer usuarioId, String modulo, String accion,
                                                         String entidad, String resultado, Boolean activo,
                                                         Pageable pageable) {
        Page<BitacoraResponse> page = bitacoraRepository
                .listarBitacoras(usuarioId, modulo, accion, entidad, resultado, activo, pageable)
                .map(projection -> globalMapper.map(projection, BitacoraResponse.class));
        return new CustomPage<>(page);
    }

    @Override
    @Transactional
    public ResponseEntity<MessageResponse> registrar(BitacoraRegistroRequest request) {
        BitacoraEntity entity = new BitacoraEntity();
        entity.setFecha(LocalDateTime.now());
        entity.setUsuarioId(request.getUsuarioId());
        entity.setModulo(request.getModulo().trim());
        entity.setAccion(request.getAccion().trim());
        entity.setEntidad(request.getEntidad().trim());
        entity.setRegistroId(request.getRegistroId());
        entity.setDetalle(request.getDetalle().trim());
        entity.setResultado(request.getResultado().trim());
        entity.setActivo(Boolean.TRUE);
        bitacoraRepository.save(entity);
        return MessageResponse.setResponse(Boolean.TRUE, HttpStatus.CREATED, MSG_REGISTRADA);
    }
}

