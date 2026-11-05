package com.proyecto.integrador.service;

import com.proyecto.integrador.model.response.ClimaResponse;

public interface ClimaService {
    ClimaResponse consultar(String destino);
}