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
public class NavieraRegistroRequest {

    @NotNull(message = "{message.required}") private Integer navieraId;
    @NotBlank(message = "{message.required}") @Size(max = 30, message = "{message.longitudmax}") private String codigo;
    @NotBlank(message = "{message.required}") @Size(max = 150, message = "{message.longitudmax}") private String nombre;
    @NotBlank(message = "{message.required}") @Size(max = 100, message = "{message.longitudmax}") private String pais;
    @Size(max = 150, message = "{message.longitudmax}") private String contacto;
    @Size(max = 150, message = "{message.longitudmax}") private String correo;
    @Size(max = 30, message = "{message.longitudmax}") private String telefono;
    @Size(max = 250, message = "{message.longitudmax}") private String sitioWeb;
}
