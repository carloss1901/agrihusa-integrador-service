package com.proyecto.integrador.repository;

import com.proyecto.integrador.model.entity.SituacionEntity;
import com.proyecto.integrador.model.projection.SituacionProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SituacionRepository extends JpaRepository<SituacionEntity, Integer> {
    java.util.List<SituacionEntity> findAllByActivoTrueOrderByDescripcionAsc();

    boolean existsByDescripcionIgnoreCase(String descripcion);

    boolean existsByDescripcionIgnoreCaseAndSituacionIdNot(String descripcion, Integer situacionId);

    @Query(value = """
            SELECT
                s.situacion_id AS situacionId,
                s.descripcion AS descripcion,
                s.activo AS activo,
                CASE WHEN s.activo = 1 THEN 'Activo' ELSE 'Inactivo' END AS estadoDsc
            FROM situacion s
            WHERE (:descripcion IS NULL OR :descripcion = ''
                   OR LOWER(s.descripcion) LIKE LOWER(CONCAT('%', :descripcion, '%')))
              AND (:activo IS NULL OR s.activo = :activo)
            ORDER BY s.situacion_id
            """,
            countQuery = """
                    SELECT COUNT(*)
                    FROM situacion s
                    WHERE (:descripcion IS NULL OR :descripcion = ''
                           OR LOWER(s.descripcion) LIKE LOWER(CONCAT('%', :descripcion, '%')))
                      AND (:activo IS NULL OR s.activo = :activo)
                    """,
            nativeQuery = true)
    Page<SituacionProjection> listarSituaciones(
            @Param("descripcion") String descripcion,
            @Param("activo") Boolean activo,
            Pageable pageable);
}
