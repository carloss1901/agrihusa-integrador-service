package com.proyecto.integrador.repository;

import com.proyecto.integrador.model.entity.UsuarioRolEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRolRepository extends JpaRepository<UsuarioRolEntity, Integer> {
    List<UsuarioRolEntity> findAllByUsuarioIdAndActivoTrue(Integer usuarioId);

    Optional<UsuarioRolEntity> findByUsuarioIdAndRolId(
            Integer usuarioId,
            Integer rolId
    );
}
