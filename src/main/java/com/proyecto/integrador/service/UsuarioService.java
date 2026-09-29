package com.proyecto.integrador.service;

import com.proyecto.integrador.model.entity.UsuarioEntity;
import com.proyecto.integrador.model.request.UsuarioRegistroRequest;

public interface UsuarioService {

    UsuarioEntity registrar(UsuarioRegistroRequest request);
}
