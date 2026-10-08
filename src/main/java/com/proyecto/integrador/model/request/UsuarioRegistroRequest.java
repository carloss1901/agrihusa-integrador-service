package com.proyecto.integrador.model.request;

import jakarta.validation.constraints.Email;
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
public class UsuarioRegistroRequest {

    private Integer usuarioId = 0;

    @NotBlank(message = "{message.required}")
    @jakarta.validation.constraints.Pattern(regexp = "\\d{8}", message = "{message.dni}")
    private String dni;

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

    @NotNull(message = "{message.required}")
    private Integer rolId;

    public UsuarioRegistroRequest(String dni, String nombres, String apellidoPaterno,
                                  String apellidoMaterno, String correo, String telefono,
                                  Integer rolId) {
        this(0, dni, nombres, apellidoPaterno, apellidoMaterno, correo, telefono, rolId);
    }

}
