package com.proyecto.integrador.service.impl;

import com.proyecto.integrador.model.entity.PersonaEntity;
import com.proyecto.integrador.repository.PersonaRepository;
import com.proyecto.integrador.service.PersonaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonaServiceImpl implements PersonaService {

    private final PersonaRepository personaRepository;

    @Override
    public List<PersonaEntity> listar() {
        return personaRepository.findAll();
    }
}
