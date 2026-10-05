package com.proyecto.integrador.config;

import java.time.Duration;
import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {

    @Bean
    CorsConfigurationSource corsConfigurationSource(Cors cors) {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(cors.isAllowCredentials());
        configuration.setAllowedOrigins(csv(cors.getAllowOrigins()));
        configuration.setAllowedHeaders(csv(cors.getAllowHeaders()));
        configuration.setAllowedMethods(csv(cors.getAllowMethods()));
        configuration.setExposedHeaders(csv(cors.getExposedHeaders()));
        configuration.setMaxAge(Duration.ofSeconds(cors.getMaxAge()));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration(cors.getMapping(), configuration);
        return source;
    }

    private static java.util.List<String> csv(String value) {
        return Arrays.stream(value.split(","))
                .map(String::trim)
                .filter(item -> !item.isEmpty())
                .toList();
    }
}
