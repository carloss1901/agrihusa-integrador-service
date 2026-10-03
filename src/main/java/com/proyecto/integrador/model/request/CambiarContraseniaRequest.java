package com.proyecto.integrador.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CambiarContraseniaRequest {

    @NotBlank(message = "{message.required}")
    private String contraseniaActual;

    @NotBlank(message = "{message.required}")
    @Size(min = 8, max = 100, message = "{message.longitudminmax}")
    private String nuevaContrasenia;

    @NotBlank(message = "{message.required}")
    private String confirmarContrasenia;
}
