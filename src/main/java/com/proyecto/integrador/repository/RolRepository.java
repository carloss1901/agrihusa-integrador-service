package com.proyecto.integrador.repository;

import com.proyecto.integrador.model.entity.RolEntity;
import com.proyecto.integrador.model.projection.RolProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RolRepository extends JpaRepository<RolEntity, Integer> {

    boolean existsByNombreIgnoreCase(String nombre);

    @Query(value = """
        SELECT
            r.rol_id AS rolId,
            r.nombre AS nombre,
            r.descripcion AS descripcion,
            r.activo AS activo,
            CASE WHEN r.activo = 1 THEN 'Activo' ELSE 'Inactivo' END AS estadoDsc
        FROM rol r
        WHERE (:nombre IS NULL OR :nombre = '' OR LOWER(r.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')))
          AND (:activo IS NULL OR r.activo = :activo)
        ORDER BY r.rol_id
        """,
        countQuery = """
            SELECT COUNT(*)
            FROM rol r
            WHERE (:nombre IS NULL OR :nombre = '' OR LOWER(r.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')))
              AND (:activo IS NULL OR r.activo = :activo)
            """, nativeQuery = true)
    Page<RolProjection> listarRoles(@Param("nombre") String nombre,
                                    @Param("activo") Boolean activo,
                                    Pageable pageable);
}
