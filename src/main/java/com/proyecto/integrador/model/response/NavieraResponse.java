package com.proyecto.integrador.model.response;

import com.proyecto.integrador.model.entity.NavieraEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NavieraResponse {

    private Integer navieraId;
    private String codigo;
    private String nombre;
    private String pais;
    private String contacto;
    private String correo;
    private String telefono;
    private String sitioWeb;
    private Boolean activo;
    private String estadoDsc;
    public static NavieraResponse from(NavieraEntity e) {
        return new NavieraResponse(e.getNavieraId(), e.getCodigo(), e.getNombre(), e.getPais(),
                e.getContacto(), e.getCorreo(), e.getTelefono(), e.getSitioWeb(), e.getActivo(),
                Boolean.TRUE.equals(e.getActivo()) ? "Activo" : "Inactivo");
    }
}
