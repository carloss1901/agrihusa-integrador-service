package com.proyecto.integrador.security;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Acceso a los datos del JWT de la petición actual.
 * Los métodos devuelven null o listas vacías cuando no existe una autenticación.
 */
public final class JwtData {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private JwtData() {
    }

    private static Claims getClaims() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof UsernamePasswordAuthenticationToken token
                && token.getDetails() instanceof Claims claims) {
            return claims;
        }

        return null;
    }

    public static String getUsuario() {
        Claims claims = getClaims();
        return claims != null ? claims.getSubject() : null;
    }

    public static Integer getUsuarioId() {
        return getIntegerClaim("usuarioId");
    }

    public static String getCorreo() {
        Claims claims = getClaims();
        return claims != null ? claims.get("correo", String.class) : null;
    }

    public static Date getFechaExpiracion() {
        Claims claims = getClaims();
        return claims != null ? claims.getExpiration() : null;
    }

    public static Date getFechaEmision() {
        Claims claims = getClaims();
        return claims != null ? claims.getIssuedAt() : null;
    }

    public static List<Map<String, Object>> getRoles() {
        return getListClaim("roles");
    }

    /**
     * Devuelve los módulos de todos los roles del usuario.
     * Si un usuario tiene el mismo módulo en más de un rol, se conserva una sola entrada.
     */
    public static List<Map<String, Object>> getModulos() {
        Map<Integer, Map<String, Object>> modulosPorId = new java.util.LinkedHashMap<>();

        for (Map<String, Object> rol : getRoles()) {
            Object modulos = rol.get("modulos");
            if (!(modulos instanceof List<?> listaModulos)) {
                continue;
            }

            for (Object modulo : listaModulos) {
                if (!(modulo instanceof Map<?, ?> mapa)) {
                    continue;
                }

                Map<String, Object> moduloMap = OBJECT_MAPPER.convertValue(
                        mapa, new TypeReference<Map<String, Object>>() {});
                Integer moduloId = toInteger(moduloMap.get("moduloId"));
                if (moduloId != null) {
                    modulosPorId.putIfAbsent(moduloId, moduloMap);
                }
            }
        }

        return List.copyOf(modulosPorId.values());
    }

    private static Integer getIntegerClaim(String claimName) {
        Claims claims = getClaims();
        return claims == null ? null : toInteger(claims.get(claimName));
    }

    private static List<Map<String, Object>> getListClaim(String claimName) {
        Claims claims = getClaims();
        if (claims == null || claims.get(claimName) == null) {
            return Collections.emptyList();
        }

        return OBJECT_MAPPER.convertValue(
                claims.get(claimName),
                new TypeReference<List<Map<String, Object>>>() {});
    }

    private static Integer toInteger(Object value) {
        if (value instanceof Number number) {
            return number.intValue();
        }
        if (value instanceof String text) {
            try {
                return Integer.valueOf(text);
            } catch (NumberFormatException ignored) {
                return null;
            }
        }
        return null;
    }
}
