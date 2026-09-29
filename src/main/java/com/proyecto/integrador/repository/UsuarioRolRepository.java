package com.proyecto.integrador.repository;

import com.proyecto.integrador.model.entity.UsuarioRolEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UsuarioRolRepository extends JpaRepository<UsuarioRolEntity, Integer> {
    List<UsuarioRolEntity> findAllByUsuarioIdAndActivoTrue(Integer usuarioId);
}
