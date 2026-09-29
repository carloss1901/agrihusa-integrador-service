package com.proyecto.integrador.repository;

import com.proyecto.integrador.model.entity.PermisoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PermisoRepository extends JpaRepository<PermisoEntity, Integer> {

    Optional<PermisoEntity> findByAccionAndActivoTrue(String accion);
}
