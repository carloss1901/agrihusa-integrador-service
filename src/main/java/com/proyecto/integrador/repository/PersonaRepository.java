package com.proyecto.integrador.repository;

import com.proyecto.integrador.model.entity.PersonaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonaRepository extends JpaRepository<PersonaEntity, Integer> {
}
