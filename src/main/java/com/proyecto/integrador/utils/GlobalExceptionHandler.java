package com.proyecto.integrador.utils;

import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<Object> handleApiException(ApiException exception) {
        LOGGER.warn(exception.getErrorGenerico().getCodigo(), exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getErrorGenerico());
    }

    @ExceptionHandler(CustomNotFoundException.class)
    public ResponseEntity<ErrorGenerico> handleCustomNotFoundException(CustomNotFoundException exception) {
        ApiException apiException = new ApiException(
                TypeMessage.DANGER,
                exception.getMessage(),
                HttpStatus.NOT_FOUND
        );
        LOGGER.warn(exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiException.getErrorGenerico());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorGenerico> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException exception) {
        List<ErrorCampo> errores = new ArrayList<>();
        exception.getBindingResult().getAllErrors().forEach(error -> {
            ErrorCampo errorCampo = new ErrorCampo();
            errorCampo.setCampo(((FieldError) error).getField());
            errorCampo.setMensaje(error.getDefaultMessage());
            errores.add(errorCampo);
        });

        ApiException apiException = new ApiException(
                TypeMessage.DANGER,
                "Errores de validación",
                HttpStatus.BAD_REQUEST,
                errores
        );
        LOGGER.warn(exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiException.getErrorGenerico());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorGenerico> handleConstraintViolationException(
            ConstraintViolationException exception) {
        ApiException apiException = new ApiException(
                TypeMessage.DANGER,
                exception.getMessage(),
                HttpStatus.BAD_REQUEST
        );
        LOGGER.warn(exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiException.getErrorGenerico());
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<ErrorGenerico> handleSQLException(
            SQLIntegrityConstraintViolationException exception) {
        ApiException apiException = new ApiException(
                TypeMessage.DANGER,
                exception.getMessage(),
                HttpStatus.BAD_REQUEST
        );
        LOGGER.warn("{} | {}", exception.getMessage(), Arrays.toString(exception.getStackTrace()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiException.getErrorGenerico());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorGenerico> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException exception) {
        ApiException apiException = new ApiException(
                TypeMessage.DANGER,
                exception.getMessage(),
                HttpStatus.BAD_REQUEST
        );
        LOGGER.warn(exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiException.getErrorGenerico());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorGenerico> handleDataIntegrity(DataIntegrityViolationException exception) {
        ApiException apiException = new ApiException(
                TypeMessage.DANGER,
                "No se pudo procesar la solicitud por datos no v\u00e1lidos o duplicados",
                HttpStatus.CONFLICT
        );
        LOGGER.warn("Error de datos: {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(apiException.getErrorGenerico());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorGenerico> handleIllegalArgument(IllegalArgumentException exception) {
        ApiException apiException = new ApiException(
                TypeMessage.DANGER,
                "Los par\u00e1metros de la solicitud no son v\u00e1lidos",
                HttpStatus.BAD_REQUEST
        );
        LOGGER.warn("Par\u00e1metros inv\u00e1lidos: {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiException.getErrorGenerico());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorGenerico> handleUnexpected(Exception exception) {
        ApiException apiException = new ApiException(
                TypeMessage.DANGER,
                "Ocurri\u00f3 un error interno al procesar la solicitud",
                HttpStatus.INTERNAL_SERVER_ERROR
        );
        LOGGER.error("Error no controlado", exception);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiException.getErrorGenerico());
    }
}
