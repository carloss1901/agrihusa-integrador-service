package com.proyecto.integrador.service.impl;

import com.proyecto.integrador.model.response.ClimaResponse;
import com.proyecto.integrador.service.ClimaService;
import com.proyecto.integrador.utils.ApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import java.util.function.Supplier;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ClimaServiceImpl implements ClimaService {

    private final RestTemplate restTemplate;
    private final String geocodingUrl;
    private final String forecastUrl;
    private final Duration cacheDuration;
    private final Map<String, CachedWeather> cache = new ConcurrentHashMap<>();

    public ClimaServiceImpl(
            @Value("${open-meteo.geocoding-url:https://geocoding-api.open-meteo.com/v1/search}")
            String geocodingUrl,
            @Value("${open-meteo.forecast-url:https://api.open-meteo.com/v1/forecast}")
            String forecastUrl,
            @Value("${open-meteo.connect-timeout-ms:3000}")
            int connectTimeout,
            @Value("${open-meteo.read-timeout-ms:5000}")
            int readTimeout,
            @Value("${open-meteo.cache-seconds:300}")
            long cacheSeconds
    ) {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(connectTimeout);
        factory.setReadTimeout(readTimeout);

        this.restTemplate = new RestTemplate(factory);
        this.geocodingUrl = geocodingUrl;
        this.forecastUrl = forecastUrl;
        this.cacheDuration = Duration.ofSeconds(cacheSeconds);
    }

    @Override
    public ClimaResponse consultar(String destino) {
        String clave = destino.trim().toLowerCase(Locale.ROOT);
        CachedWeather cached = cache.get(clave);

        if (cached != null && cached.expiresAt().isAfter(Instant.now())) {
            return conEstadoCache(cached.value(), true);
        }

        try {
            Map<?, ?> ubicacion = buscarUbicacion(destino);
            ClimaResponse response = consultarPronostico(destino, ubicacion);

            cache.put(
                    clave,
                    new CachedWeather(
                            response,
                            Instant.now().plus(cacheDuration)
                    )
            );

            return response;
        } catch (ApiException exception) {
            throw exception;
        } catch (ResourceAccessException exception) {
            throw new ApiException(
                    "El servicio meteorológico no está disponible temporalmente",
                    HttpStatus.SERVICE_UNAVAILABLE
            );
        } catch (HttpStatusCodeException exception) {
            if (exception.getStatusCode().value() == 429
                    || exception.getStatusCode().is5xxServerError()) {
                throw new ApiException(
                        "El servicio meteorológico no está disponible temporalmente",
                        HttpStatus.SERVICE_UNAVAILABLE
                );
            }

            throw new ApiException(
                    "Open-Meteo rechazó la solicitud",
                    HttpStatus.BAD_GATEWAY
            );
        } catch (RestClientException | IllegalStateException exception) {
            throw new ApiException(
                    "Open-Meteo devolvió una respuesta no válida",
                    HttpStatus.BAD_GATEWAY
            );
        }
    }

    private Map<?, ?> buscarUbicacion(String destino) {
        String url = geocodingUrl
                + "?name={destino}&count=1&language=es&format=json";

        Map<?, ?> body = ejecutarConReintento(
                () -> restTemplate.getForObject(
                        url,
                        Map.class,
                        destino
                )
        );

        if (body == null || !(body.get("results") instanceof List<?> resultados)
                || resultados.isEmpty()
                || !(resultados.get(0) instanceof Map<?, ?> ubicacion)) {
            throw new ApiException(
                    "No se encontró información geográfica para el destino",
                    HttpStatus.NOT_FOUND
            );
        }

        return ubicacion;
    }

    private ClimaResponse consultarPronostico(
            String destinoSolicitado,
            Map<?, ?> ubicacion
    ) {
        double latitud = numeroObligatorio(ubicacion.get("latitude"));
        double longitud = numeroObligatorio(ubicacion.get("longitude"));

        String url = forecastUrl
                + "?latitude={latitud}"
                + "&longitude={longitud}"
                + "&current=temperature_2m,precipitation,wind_speed_10m,weather_code"
                + "&timezone=auto"
                + "&temperature_unit=celsius"
                + "&wind_speed_unit=kmh"
                + "&precipitation_unit=mm";

        Map<?, ?> body = ejecutarConReintento(
                () -> restTemplate.getForObject(
                        url,
                        Map.class,
                        latitud,
                        longitud
                )
        );

        if (body == null || !(body.get("current") instanceof Map<?, ?> current)) {
            throw new IllegalStateException("Respuesta meteorológica incompleta");
        }

        Double temperatura = numero(current.get("temperature_2m"));
        Double precipitacion = numero(current.get("precipitation"));
        Double viento = numero(current.get("wind_speed_10m"));
        Integer codigo = entero(current.get("weather_code"));

        if (temperatura == null || precipitacion == null
                || viento == null || codigo == null) {
            throw new IllegalStateException("Campos meteorológicos incompletos");
        }

        return new ClimaResponse(
                texto(ubicacion.get("name"), destinoSolicitado),
                texto(ubicacion.get("country"), ""),
                latitud,
                longitud,
                temperatura,
                precipitacion,
                viento,
                codigo,
                describirCodigo(codigo),
                texto(body.get("timezone"), ""),
                texto(current.get("time"), ""),
                Instant.now(),
                false
        );
    }

    private ClimaResponse conEstadoCache(
            ClimaResponse value,
            boolean desdeCache
    ) {
        return new ClimaResponse(
                value.destino(),
                value.pais(),
                value.latitud(),
                value.longitud(),
                value.temperaturaCelsius(),
                value.precipitacionMm(),
                value.velocidadVientoKmh(),
                value.codigoTiempo(),
                value.descripcion(),
                value.zonaHoraria(),
                value.fechaClima(),
                value.fechaConsulta(),
                desdeCache
        );
    }

    private <T> T ejecutarConReintento(Supplier<T> solicitud) {
        try {
            return solicitud.get();
        } catch (ResourceAccessException exception) {
            return solicitud.get();
        } catch (HttpStatusCodeException exception) {
            if (exception.getStatusCode().value() == 429
                    || exception.getStatusCode().is5xxServerError()) {
                return solicitud.get();
            }

            throw exception;
        }
    }

    private Double numero(Object value) {
        return value instanceof Number number
                ? number.doubleValue()
                : null;
    }

    private double numeroObligatorio(Object value) {
        Double numero = numero(value);

        if (numero == null) {
            throw new IllegalStateException("Coordenada no válida");
        }

        return numero;
    }

    private Integer entero(Object value) {
        return value instanceof Number number
                ? number.intValue()
                : null;
    }

    private String texto(Object value, String valorPredeterminado) {
        return value instanceof String texto && !texto.isBlank()
                ? texto
                : valorPredeterminado;
    }

    private String describirCodigo(int codigo) {
        return switch (codigo) {
            case 0 -> "Despejado";
            case 1, 2, 3 -> "Parcialmente nublado";
            case 45, 48 -> "Niebla";
            case 51, 53, 55, 56, 57 -> "Llovizna";
            case 61, 63, 65, 66, 67 -> "Lluvia";
            case 71, 73, 75, 77 -> "Nieve";
            case 80, 81, 82 -> "Chubascos";
            case 85, 86 -> "Chubascos de nieve";
            case 95, 96, 99 -> "Tormenta";
            default -> "Condición no determinada";
        };
    }

    private record CachedWeather(
            ClimaResponse value,
            Instant expiresAt
    ) {
    }
}