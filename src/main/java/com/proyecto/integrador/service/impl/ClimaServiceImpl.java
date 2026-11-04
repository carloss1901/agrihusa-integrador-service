package com.proyecto.integrador.service.impl;

import com.proyecto.integrador.model.response.ClimaResponse;
import com.proyecto.integrador.service.ClimaService;
import com.proyecto.integrador.utils.CustomNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ClimaServiceImpl implements ClimaService {
    private final RestTemplate restTemplate;
    private final String baseUrl;
    private final Duration cacheDuration;
    private final Map<String, CachedWeather> cache = new ConcurrentHashMap<>();

    public ClimaServiceImpl(@Value("${open-meteo.base-url:https://api.open-meteo.com/v1/forecast}") String baseUrl,
                            @Value("${open-meteo.connect-timeout-ms:3000}") int connectTimeout,
                            @Value("${open-meteo.read-timeout-ms:5000}") int readTimeout,
                            @Value("${open-meteo.cache-seconds:300}") long cacheSeconds) {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(connectTimeout);
        factory.setReadTimeout(readTimeout);
        this.restTemplate = new RestTemplate(factory);
        this.baseUrl = baseUrl;
        this.cacheDuration = Duration.ofSeconds(cacheSeconds);
    }

    @Override
    public ClimaResponse consultar(double latitud, double longitud) {
        String key = latitud + ":" + longitud;
        CachedWeather cached = cache.get(key);
        if (cached != null && cached.expiresAt().isAfter(Instant.now())) {
            ClimaResponse value = cached.value();
            return new ClimaResponse(value.latitud(), value.longitud(), value.temperaturaCelsius(),
                    value.velocidadVientoKmh(), value.codigoTiempo(), value.fechaConsulta(), true);
        }
        try {
            String url = baseUrl + "?latitude={latitud}&longitude={longitud}&current_weather=true";
            Map<?, ?> body = restTemplate.getForObject(url, Map.class, latitud, longitud);
            if (body == null || !(body.get("current_weather") instanceof Map<?, ?> weather)) {
                throw new IllegalStateException("Open-Meteo no devolvi\u00f3 clima actual");
            }
            ClimaResponse response = new ClimaResponse(latitud, longitud, number(weather.get("temperature")),
                    number(weather.get("windspeed")), integer(weather.get("weathercode")), Instant.now(), false);
            cache.put(key, new CachedWeather(response, Instant.now().plus(cacheDuration)));
            return response;
        } catch (RuntimeException exception) {
            throw new CustomNotFoundException("No fue posible consultar el servicio meteorol\u00f3gico");
        }
    }

    private Double number(Object value) { return value instanceof Number number ? number.doubleValue() : null; }
    private Integer integer(Object value) { return value instanceof Number number ? number.intValue() : null; }
    private record CachedWeather(ClimaResponse value, Instant expiresAt) { }
}
