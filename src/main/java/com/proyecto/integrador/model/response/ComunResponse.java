package com.proyecto.integrador.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComunResponse {

    private Integer id;
    private String descripcion;
    private Integer value2;

    public ComunResponse(Integer id, String descripcion) {
        this(id, descripcion, null);
    }
}
