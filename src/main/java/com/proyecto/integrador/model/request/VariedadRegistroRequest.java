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
public class VariedadRegistroRequest {

    @NotNull(message = "{message.required}")
    private Integer variedadId;

    @NotNull(message = "{message.required}")
    private Integer productoId;

    @NotBlank(message = "{message.required}")
    @Size(max = 150, message = "{message.longitudmax}")
    private String nombre;
}
