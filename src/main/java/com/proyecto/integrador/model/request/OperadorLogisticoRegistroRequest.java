package com.proyecto.integrador.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OperadorLogisticoRegistroRequest {

    @NotNull(message = "{message.required}")
    private Integer operadorLogisticoId;

    @NotBlank(message = "{message.required}")
    @Size(max = 11, message = "{message.longitudmax}")
    private String ruc;

    @NotBlank(message = "{message.required}")
    @Size(max = 200, message = "{message.longitudmax}")
    private String razonSocial;

    @Size(max = 200, message = "{message.longitudmax}")
    private String nombreComercial;

    @Size(max = 150, message = "{message.longitudmax}")
    private String contacto;

    @Size(max = 150, message = "{message.longitudmax}")
    private String correo;

    @Size(max = 30, message = "{message.longitudmax}")
    private String telefono;

    @Size(max = 250, message = "{message.longitudmax}")
    private String direccion;
}
