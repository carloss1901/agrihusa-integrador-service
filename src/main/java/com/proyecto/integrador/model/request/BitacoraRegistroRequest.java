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
public class BitacoraRegistroRequest {

    private Integer usuarioId;

    @NotBlank(message = "{message.required}")
    @Size(max = 50, message = "{message.longitudmax}")
    private String modulo;

    @NotBlank(message = "{message.required}")
    @Size(max = 50, message = "{message.longitudmax}")
    private String accion;

    @NotBlank(message = "{message.required}")
    @Size(max = 100, message = "{message.longitudmax}")
    private String entidad;

    private Integer registroId;

    @NotBlank(message = "{message.required}")
    @Size(max = 500, message = "{message.longitudmax}")
    private String detalle;

    @NotBlank(message = "{message.required}")
    @Size(max = 20, message = "{message.longitudmax}")
    private String resultado;
}
