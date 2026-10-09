package com.proyecto.integrador.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PerfilUsuarioActualizarRequest {

    @NotBlank(message = "{message.required}")
    @Size(max = 80, message = "{message.longitudmax}")
    private String nombres;

    @NotBlank(message = "{message.required}")
    @Size(max = 80, message = "{message.longitudmax}")
    private String apellidoPaterno;

    @NotBlank(message = "{message.required}")
    @Size(max = 80, message = "{message.longitudmax}")
    private String apellidoMaterno;

    @NotBlank(message = "{message.required}")
    @Email(message = "{message.email}")
    @Size(max = 120, message = "{message.longitudmax}")
    private String correo;

    @Size(max = 20, message = "{message.longitudmax}")
    private String telefono;
}