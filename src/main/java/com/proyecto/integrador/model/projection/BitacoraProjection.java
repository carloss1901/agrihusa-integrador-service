package com.proyecto.integrador.model.projection;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface BitacoraProjection {
    Integer getBitacoraId();
    LocalDateTime getFecha();
    Integer getUsuarioId();
    String getNombreUsuario();
    String getModulo();
    String getAccion();
    String getEntidad();
    Integer getRegistroId();
    String getDetalle();
    String getResultado();
    Boolean getActivo();
    LocalDate getFechaCreacion();
    LocalDate getFechaModificacion();
}
