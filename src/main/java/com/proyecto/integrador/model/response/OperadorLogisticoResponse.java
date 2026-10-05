package com.proyecto.integrador.model.response;

import com.proyecto.integrador.model.entity.OperadorLogisticoEntity;
import com.proyecto.integrador.model.projection.OperadorLogisticoProjection;

import java.time.LocalDate;

public record OperadorLogisticoResponse(
        Integer operadorLogisticoId,
        String ruc,
        String razonSocial,
        String nombreComercial,
        String contacto,
        String correo,
        String telefono,
        String direccion,
        Boolean activo,
        String estadoDsc,
        LocalDate fechaCreacion,
        LocalDate fechaModificacion
) {

    public static OperadorLogisticoResponse from(OperadorLogisticoProjection projection) {
        return new OperadorLogisticoResponse(
                projection.getOperadorLogisticoId(),
                projection.getRuc(),
                projection.getRazonSocial(),
                projection.getNombreComercial(),
                projection.getContacto(),
                projection.getCorreo(),
                projection.getTelefono(),
                projection.getDireccion(),
                projection.getActivo(),
                projection.getEstadoDsc(),
                projection.getFechaCreacion(),
                projection.getFechaModificacion()
        );
    }

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
                Boolean.TRUE.equals(entity.getActivo()) ? "Activo" : "Inactivo",
                entity.getFechaCreacion(),
                entity.getFechaModificacion()
        );
    }
}
