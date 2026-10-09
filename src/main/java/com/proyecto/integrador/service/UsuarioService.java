package com.proyecto.integrador.service;

import com.proyecto.integrador.model.entity.UsuarioEntity;
import com.proyecto.integrador.model.request.UsuarioRegistroRequest;
import com.proyecto.integrador.model.request.CambiarContraseniaRequest;
import org.springframework.http.ResponseEntity;
import com.proyecto.integrador.model.response.UsuarioResponse;
import com.proyecto.integrador.utils.CustomPage;
import org.springframework.data.domain.Pageable;
import com.proyecto.integrador.model.request.UsuarioActualizarRequest;
import com.proyecto.integrador.model.request.PerfilUsuarioActualizarRequest;

public interface UsuarioService {

    CustomPage<UsuarioResponse> listarUsuarios(
            String texto,
            Integer rolId,
            Boolean activo,
            Pageable pageable
    );

    UsuarioResponse obtenerPorId(Integer usuarioId);

    ResponseEntity<Object> actualizar(
            UsuarioActualizarRequest request
    );

    ResponseEntity<Object> cambiarEstado(
            Integer usuarioId,
            Boolean activo
    );

    ResponseEntity<Object> registrar(
            UsuarioRegistroRequest request
    );

    ResponseEntity<Object> actualizarPerfil(
            PerfilUsuarioActualizarRequest request
    );

    ResponseEntity<Object> cambiarContrasenia(
            CambiarContraseniaRequest request
    );
}