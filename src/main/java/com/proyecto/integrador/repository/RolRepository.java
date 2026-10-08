package com.proyecto.integrador.repository;

import com.proyecto.integrador.model.entity.RolEntity;
import com.proyecto.integrador.model.projection.RolProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface RolRepository extends JpaRepository<RolEntity, Integer> {

    boolean existsByNombreIgnoreCase(String nombre);

    List<RolEntity> findAllByActivoTrueOrderByNombreAsc();

    List<RolEntity> findAllByRolIdInAndActivoTrue(Collection<Integer> rolIds);

    @Query(value = """
        SELECT
            r.rol_id AS rolId,
            r.nombre AS nombre,
            r.descripcion AS descripcion,
            r.es_sistema AS esSistema,
            r.activo AS activo,
            COUNT(CASE WHEN rp.activo = 1 THEN 1 END) AS cantidadPermisos,
            CASE WHEN r.activo = 1 THEN 'Activo' ELSE 'Inactivo' END AS estadoDsc
        FROM rol r
        LEFT JOIN rol_permiso rp ON rp.rol_id = r.rol_id
        WHERE (:nombre IS NULL OR :nombre = '' OR LOWER(r.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')))
          AND (:activo IS NULL OR r.activo = :activo)
        GROUP BY r.rol_id, r.nombre, r.descripcion, r.es_sistema, r.activo
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
