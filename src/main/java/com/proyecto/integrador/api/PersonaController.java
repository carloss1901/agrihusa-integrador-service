package com.proyecto.integrador.api;

import com.proyecto.integrador.model.entity.PersonaEntity;
import com.proyecto.integrador.service.PersonaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/personas")
@RequiredArgsConstructor
public class PersonaController {

    private final PersonaService personaService;

    @GetMapping
    public List<PersonaEntity> listar() {
        return personaService.listar();
    }
}
