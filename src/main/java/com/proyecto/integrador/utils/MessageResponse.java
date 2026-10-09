package com.proyecto.integrador.utils;

import jakarta.validation.ConstraintViolation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "MessageResponse")
public class MessageResponse {

    private Boolean success;
    private String message;
    private Integer status;
    private Object data;

    public static MessageResponse body(
            boolean success,
            HttpStatus status,
            String message,
            @Nullable Object data) {
        return new MessageResponse(success, message, status.value(), data);
    }

    public static ResponseEntity<Object> setResponse(
            @Nullable boolean success,
            HttpStatus status,
            @Nullable String message,
            @Nullable Object data) {
        return new ResponseEntity<>(
                body(success, status, message, data),
                status
        );
    }

    public static ResponseEntity<Object> setResponse(
            HttpStatus status,
            String message,
            @Nullable Object data) {
        return setResponse(Boolean.FALSE, status, message, data);
    }

    public static ResponseEntity<Object> setResponse(
            boolean success,
            HttpStatus status,
            String message) {
        return setResponse(success, status, message, null);
    }

    public static ResponseEntity<Object> setResponse(HttpStatus status, String message) {
        return setResponse(Boolean.FALSE, status, message, null);
    }

    public static ResponseEntity<Object> setResponse(
            boolean success,
            String message,
            @Nullable Object data) {
        HttpStatus status = success ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return setResponse(success, status, message, data);
    }

    public static ResponseEntity<Object> setResponse(boolean success, String message) {
        return setResponse(success, message, null);
    }

    public static ResponseEntity<Object> setResponse(
            Set<? extends ConstraintViolation<?>> violations) {
        String message = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));
        return setResponse(Boolean.FALSE, HttpStatus.BAD_REQUEST, message, null);
    }
}
