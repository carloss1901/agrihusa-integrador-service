package com.proyecto.integrador.model.response;

import java.time.Instant;

public record ClimaResponse(double latitud, double longitud, Double temperaturaCelsius,
                            Double velocidadVientoKmh, Integer codigoTiempo,
                            Instant fechaConsulta, boolean desdeCache) {
}
