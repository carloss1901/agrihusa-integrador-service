package com.proyecto.integrador.model.response;

import com.proyecto.integrador.model.entity.ClienteEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteResponse {

    private Integer clienteId;
    private String tipoDocumento;
    private String numeroDocumento;
    private String razonSocial;
    private String nombreComercial;
    private String contacto;
    private String correo;
    private String telefono;
    private String direccion;
    private String pais;
    private Boolean activo;
    private String estadoDsc;
    public static ClienteResponse from(ClienteEntity e) {
        return new ClienteResponse(e.getClienteId(), e.getTipoDocumento(), e.getNumeroDocumento(), e.getRazonSocial(),
                e.getNombreComercial(), e.getContacto(), e.getCorreo(), e.getTelefono(), e.getDireccion(), e.getPais(), e.getActivo(),
                Boolean.TRUE.equals(e.getActivo()) ? "Activo" : "Inactivo");
    }
}
