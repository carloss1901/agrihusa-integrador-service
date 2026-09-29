package com.proyecto.integrador.repository;

import com.proyecto.integrador.model.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Integer> {

    boolean existsByDni(String dni);

    boolean existsByUsuario(String usuario);

    boolean existsByCorreo(String correo);

    Optional<UsuarioEntity> findByUsuarioAndActivoTrue(String usuario);
}
