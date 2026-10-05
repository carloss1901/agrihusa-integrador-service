package com.proyecto.integrador.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
    @NotBlank(message = "{message.required}") @Pattern(regexp = "^\\s*[A-Za-z0-9-]+\\s*$", message = "{message.codigonaviera}") @Size(max = 30, message = "{message.longitudmax}") private String codigo;
    @NotBlank(message = "{message.required}") @Size(max = 150, message = "{message.longitudmax}") private String nombre;
    @NotBlank(message = "{message.required}") @Size(max = 100, message = "{message.longitudmax}") private String pais;
    @Size(max = 150, message = "{message.longitudmax}") private String contacto;
    @Email(message = "{message.email}") @Size(max = 150, message = "{message.longitudmax}") private String correo;
    @Pattern(regexp = "^[0-9+\\s()-]*$", message = "{message.telefono}") @Size(max = 30, message = "{message.longitudmax}") private String telefono;
    @Pattern(regexp = "^\\s*$|^\\s*https?://\\S+\\s*$", message = "{message.url}") @Size(max = 250, message = "{message.longitudmax}") private String sitioWeb;

    // Se recorta antes de validar para que @Email no rechace espacios al inicio o al final
    public void setCorreo(String correo) { this.correo = correo == null ? null : correo.trim(); }
}
