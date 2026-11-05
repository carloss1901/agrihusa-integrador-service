package com.proyecto.integrador.model.response;

import java.time.Instant;

public record ClimaResponse(
        String destino,
        String pais,
        double latitud,
        double longitud,
        Double temperaturaCelsius,
        Double precipitacionMm,
        Double velocidadVientoKmh,
        Integer codigoTiempo,
        String descripcion,
        String zonaHoraria,
        String fechaClima,
        Instant fechaConsulta,
        boolean desdeCache
) {
}