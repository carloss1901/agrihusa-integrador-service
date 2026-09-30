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
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

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
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (ExpiredJwtException ignored) {
            request.setAttribute(JWT_ERROR_ATTRIBUTE, "El token ha expirado");
        } catch (RuntimeException ignored) {
            request.setAttribute(JWT_ERROR_ATTRIBUTE, "El token no es válido");
            // La petición continuará sin autenticación y Spring Security responderá 401.
        }
    }

    private List<SimpleGrantedAuthority> obtenerAuthorities(Claims claims) {
        Object rolesClaim = claims.get("roles");
        if (!(rolesClaim instanceof Collection<?> roles)) {
            return List.of();
        }

        return roles.stream()
                .filter(java.util.Map.class::isInstance)
                .map(java.util.Map.class::cast)
                .map(role -> role.get("nombre"))
                .filter(String.class::isInstance)
                .map(String.class::cast)
                .map(nombre -> new SimpleGrantedAuthority("ROLE_" + nombre))
                .toList();
    }
}
