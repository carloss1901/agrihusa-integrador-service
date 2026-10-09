package com.proyecto.integrador.model.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DespachoRegistroRequest {

    @NotNull(message = "{message.required}")
    private Integer despachoId;

    @NotNull(message = "{message.required}")
    private LocalDate fechaDespacho;

    @NotNull(message = "{message.required}")
    private LocalDate fechaEstimadaLlegada;

    @NotNull(message = "{message.required}")
    private Integer clienteId;

    @NotNull(message = "{message.required}")
    private Integer navieraId;

    @NotNull(message = "{message.required}")
    private Integer destinoId;

    @NotNull(message = "{message.required}")
    private Integer operadorLogisticoId;

    @NotNull(message = "{message.required}")
    private Integer puertoLlegadaId;

    @NotNull(message = "{message.required}")
    private Integer productoId;

    @NotNull(message = "{message.required}")
    private Integer variedadId;

    @NotNull(message = "{message.required}")
    private Integer viaId;

    @NotNull(message = "{message.required}")
    private Integer situacionId;

    @NotNull(message = "{message.required}")
    @DecimalMin(value = "0.01", message = "{message.valorminimo}")
    @Digits(integer = 10, fraction = 2, message = "{message.formato}")
    private BigDecimal cantidad;

    @NotBlank(message = "{message.required}")
    @Size(max = 30, message = "{message.longitudmax}")
    private String unidadMedida;

    @NotBlank(message = "{message.required}")
    @Size(max = 20, message = "{message.longitudmax}")
    private String numeroContenedor;

    @Size(max = 500, message = "{message.longitudmax}")
    private String observaciones;
}
