package com.proyecto.integrador.repository;

import com.proyecto.integrador.model.entity.UsuarioEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UsuarioRepository
        extends JpaRepository<UsuarioEntity, Integer> {

    boolean existsByDni(String dni);

    boolean existsByUsuario(String usuario);

    boolean existsByCorreo(String correo);

    boolean existsByDniAndUsuarioIdNot(
            String dni,
            Integer usuarioId
    );

    boolean existsByCorreoAndUsuarioIdNot(
            String correo,
            Integer usuarioId
    );

    Optional<UsuarioEntity> findByUsuarioAndActivoTrue(
            String usuario
    );

    @Query("""
        SELECT u
        FROM UsuarioEntity u
        WHERE (
            :texto IS NULL
            OR UPPER(u.usuario) LIKE UPPER(CONCAT('%', :texto, '%'))
            OR UPPER(u.dni) LIKE UPPER(CONCAT('%', :texto, '%'))
            OR UPPER(u.nombres) LIKE UPPER(CONCAT('%', :texto, '%'))
            OR UPPER(u.apellidoPaterno) LIKE UPPER(CONCAT('%', :texto, '%'))
            OR UPPER(u.apellidoMaterno) LIKE UPPER(CONCAT('%', :texto, '%'))
            OR UPPER(u.correo) LIKE UPPER(CONCAT('%', :texto, '%'))
        )
        AND (
            :activo IS NULL
            OR u.activo = :activo
        )
        AND (
            :rolId IS NULL
            OR EXISTS (
                SELECT ur.usuarioRolId
                FROM UsuarioRolEntity ur
                WHERE ur.usuarioId = u.usuarioId
                  AND ur.rolId = :rolId
                  AND ur.activo = TRUE
            )
        )
        ORDER BY u.usuarioId DESC
        """)
    Page<UsuarioEntity> listarUsuarios(
            @Param("texto") String texto,
            @Param("rolId") Integer rolId,
            @Param("activo") Boolean activo,
            Pageable pageable
    );
}