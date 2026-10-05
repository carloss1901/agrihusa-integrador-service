package com.proyecto.integrador.support;

import org.springframework.http.ResponseEntity;

import java.util.Map;

public final class RespuestaTestUtils {

    private RespuestaTestUtils() {
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> cuerpo(ResponseEntity<Object> respuesta) {
        return (Map<String, Object>) respuesta.getBody();
    }

    public static String mensaje(ResponseEntity<Object> respuesta) {
        return (String) cuerpo(respuesta).get("message");
    }

    @SuppressWarnings("unchecked")
    public static <T> T data(ResponseEntity<Object> respuesta) {
        return (T) cuerpo(respuesta).get("data");
    }
}
