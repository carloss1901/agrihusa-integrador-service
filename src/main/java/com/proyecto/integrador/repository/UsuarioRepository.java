package com.proyecto.integrador.repository;

import com.proyecto.integrador.model.entity.UsuarioEntity;
import com.proyecto.integrador.model.projection.UsuarioProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Integer> {

    boolean existsByDni(String dni);

    boolean existsByCorreo(String correo);

    Optional<UsuarioEntity> findByUsuarioAndActivoTrue(String usuario);

    @Query(value = """
        SELECT
            u.usuario_id AS usuarioId,
            u.usuario AS usuario,
            u.nombres AS nombres,
            CONCAT(u.apellido_paterno, ' ', u.apellido_materno) AS apellidos,
            u.correo AS correo,
            r.rol_id AS rolId,
            r.descripcion AS rolDescripcion,
            u.es_sistema AS esSistema,
            u.ultimo_acceso AS ultimoAcceso,
            CASE WHEN u.activo = 1 THEN 1 ELSE 0 END AS estadoId,
            CASE WHEN u.es_sistema = 1 THEN 'Sistema' ELSE 'Personalizado' END AS tipo,
            u.activo AS activo,
            CASE WHEN u.activo = 1 THEN 'Activo' ELSE 'Inactivo' END AS estadoDsc
        FROM usuario u
        LEFT JOIN usuario_rol ur
            ON ur.usuario_id = u.usuario_id
           AND ur.activo = 1
        LEFT JOIN rol r
            ON r.rol_id = ur.rol_id
        WHERE (:texto IS NULL OR :texto = ''
               OR LOWER(u.dni) LIKE LOWER(CONCAT('%', :texto, '%'))
               OR LOWER(u.usuario) LIKE LOWER(CONCAT('%', :texto, '%'))
               OR LOWER(u.nombres) LIKE LOWER(CONCAT('%', :texto, '%'))
               OR LOWER(u.apellido_paterno) LIKE LOWER(CONCAT('%', :texto, '%'))
               OR LOWER(u.apellido_materno) LIKE LOWER(CONCAT('%', :texto, '%'))
               OR LOWER(u.correo) LIKE LOWER(CONCAT('%', :texto, '%')))
          AND (:activo IS NULL OR u.activo = :activo)
        ORDER BY u.usuario_id
        """,
        countQuery = """
        SELECT COUNT(*)
        FROM usuario u
        WHERE (:texto IS NULL OR :texto = ''
               OR LOWER(u.dni) LIKE LOWER(CONCAT('%', :texto, '%'))
               OR LOWER(u.usuario) LIKE LOWER(CONCAT('%', :texto, '%'))
               OR LOWER(u.nombres) LIKE LOWER(CONCAT('%', :texto, '%'))
               OR LOWER(u.apellido_paterno) LIKE LOWER(CONCAT('%', :texto, '%'))
               OR LOWER(u.apellido_materno) LIKE LOWER(CONCAT('%', :texto, '%'))
               OR LOWER(u.correo) LIKE LOWER(CONCAT('%', :texto, '%')))
          AND (:activo IS NULL OR u.activo = :activo)
        """, nativeQuery = true)
    Page<UsuarioProjection> listar(@Param("texto") String texto, @Param("activo") Boolean activo,
                                   Pageable pageable);
}
