package com.proyecto.integrador.security;

import com.proyecto.integrador.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String BEARER_PREFIX = "Bearer ";
    public static final String JWT_ERROR_ATTRIBUTE = "jwtError";

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");

        if (authorization != null
                && authorization.startsWith(BEARER_PREFIX)
                && SecurityContextHolder.getContext().getAuthentication() == null) {
            String token = authorization.substring(BEARER_PREFIX.length()).trim();
            autenticar(token, request);
        }

        filterChain.doFilter(request, response);
    }

    private void autenticar(String token, HttpServletRequest request) {
        try {
            Claims claims = jwtService.parseToken(token);
            String usuario = claims.getSubject();

            if (usuario == null || usuario.isBlank()) {
                request.setAttribute(JWT_ERROR_ATTRIBUTE, "El token no contiene un usuario válido");
                return;
            }

            List<SimpleGrantedAuthority> authorities = obtenerAuthorities(claims);
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(usuario, null, authorities);
            authentication.setDetails(claims);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (ExpiredJwtException ignored) {
            request.setAttribute(JWT_ERROR_ATTRIBUTE, "El token ha expirado");
        } catch (RuntimeException ignored) {
            request.setAttribute(JWT_ERROR_ATTRIBUTE, "El token no es válido");
            // La petición continuará sin autenticación y Spring Security responderá 401.
        }
    }

    private List<SimpleGrantedAuthority> obtenerAuthorities(Claims claims) {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        Object rolesClaim = claims.get("roles");
        if (!(rolesClaim instanceof Collection<?> roles)) {
            return authorities;
        }

        for (Object roleObject : roles) {
            if (!(roleObject instanceof Map<?, ?> role)) {
                continue;
            }

            Object nombre = role.get("nombre");
            if (nombre instanceof String nombreRol && !nombreRol.isBlank()) {
                authorities.add(new SimpleGrantedAuthority("ROLE_" + nombreRol));
            }

            Object modulosObject = role.get("modulos");
            if (!(modulosObject instanceof Collection<?> modulos)) {
                continue;
            }

            for (Object moduloObject : modulos) {
                if (!(moduloObject instanceof Map<?, ?> modulo)) {
                    continue;
                }

                Object codigoObject = modulo.get("codigo");
                Object permisosObject = modulo.get("permisos");
                if (!(codigoObject instanceof String codigoModulo)
                        || !(permisosObject instanceof Collection<?> permisos)) {
                    continue;
                }

                for (Object permisoObject : permisos) {
                    if (!(permisoObject instanceof Map<?, ?> permiso)) {
                        continue;
                    }

                    Object accionObject = permiso.get("accion");
                    if (accionObject instanceof String accion
                            && !codigoModulo.isBlank() && !accion.isBlank()) {
                        authorities.add(new SimpleGrantedAuthority(
                                "PERM_" + codigoModulo + "_" + accion));
                    }
                }
            }
        }

        return authorities;
    }
}
