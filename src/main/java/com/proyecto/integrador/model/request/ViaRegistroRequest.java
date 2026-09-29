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
public class ViaRegistroRequest {

    @NotNull(message = "{message.required}")
    private Integer viaId;

    @NotBlank(message = "{message.required}")
    @Size(max = 100, message = "{message.longitudmax}")
    private String descripcion;
}
