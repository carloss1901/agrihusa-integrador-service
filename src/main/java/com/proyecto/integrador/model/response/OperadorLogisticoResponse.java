package com.proyecto.integrador.model.response;

import com.proyecto.integrador.model.entity.OperadorLogisticoEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OperadorLogisticoResponse {

    private Integer operadorLogisticoId;
    private String ruc;
    private String razonSocial;
    private String nombreComercial;
    private String contacto;
    private String correo;
    private String telefono;
    private String direccion;
    private Boolean activo;
    private String estadoDsc;

    public static OperadorLogisticoResponse from(OperadorLogisticoEntity entity) {
        return new OperadorLogisticoResponse(
                entity.getOperadorLogisticoId(),
                entity.getRuc(),
                entity.getRazonSocial(),
                entity.getNombreComercial(),
                entity.getContacto(),
                entity.getCorreo(),
                entity.getTelefono(),
                entity.getDireccion(),
                entity.getActivo(),
                Boolean.TRUE.equals(entity.getActivo()) ? "Activo" : "Inactivo"
        );
    }
}
