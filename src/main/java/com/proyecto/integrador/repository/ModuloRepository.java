package com.proyecto.integrador.repository;

import com.proyecto.integrador.model.entity.ModuloEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ModuloRepository extends JpaRepository<ModuloEntity, Integer> {

    Optional<ModuloEntity> findByCodigoAndActivoTrue(String codigo);
}
