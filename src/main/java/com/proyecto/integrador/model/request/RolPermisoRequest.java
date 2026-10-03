package com.proyecto.integrador.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RolPermisoRequest {

    @NotBlank(message = "{message.required}")
    private String modulo;

    @NotEmpty(message = "{message.required}")
    private List<@NotBlank(message = "{message.required}") String> acciones;
}
