package com.proyecto.integrador.model.response;

import com.proyecto.integrador.model.entity.BitacoraEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BitacoraResponse {

    private Integer bitacoraId;
    private LocalDateTime fecha;
    private Integer usuarioId;
    private String modulo;
    private String accion;
    private String entidad;
    private Integer registroId;
    private String detalle;
    private String resultado;
    private Boolean activo;
    private LocalDate fechaCreacion;
    private LocalDate fechaModificacion;

    public static BitacoraResponse from(BitacoraEntity e) {
        return new BitacoraResponse(e.getBitacoraId(), e.getFecha(), e.getUsuarioId(), e.getModulo(),
                e.getAccion(), e.getEntidad(), e.getRegistroId(), e.getDetalle(), e.getResultado(),
                e.getActivo(), e.getFechaCreacion(), e.getFechaModificacion());
    }
}
