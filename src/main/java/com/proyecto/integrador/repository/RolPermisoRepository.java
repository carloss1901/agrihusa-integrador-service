package com.proyecto.integrador.repository;

import com.proyecto.integrador.model.entity.RolPermisoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RolPermisoRepository extends JpaRepository<RolPermisoEntity, Integer> {

    List<RolPermisoEntity> findAllByRolId(Integer rolId);

    Optional<RolPermisoEntity> findByRolIdAndModuloIdAndPermisoId(
            Integer rolId, Integer moduloId, Integer permisoId);
}
