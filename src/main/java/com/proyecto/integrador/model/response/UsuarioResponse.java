package com.proyecto.integrador.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResponse {

    private Integer usuarioId;
    private String usuario;
    private String nombres;
    private String apellidos;
    private String correo;
    private Integer rolId;
    private String rolDescripcion;
    private Boolean esSistema;
    private LocalDateTime ultimoAcceso;
    private Integer estadoId;
    private String tipo;
    private Boolean activo;
    private String estadoDsc;

}
