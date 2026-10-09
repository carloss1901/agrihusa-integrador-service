package com.proyecto.integrador.repository;

import com.proyecto.integrador.model.entity.ViaEntity;
import com.proyecto.integrador.model.projection.ViaProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ViaRepository extends JpaRepository<ViaEntity, Integer> {

    boolean existsByDescripcionIgnoreCase(String descripcion);

    boolean existsByDescripcionIgnoreCaseAndViaIdNot(String descripcion, Integer viaId);

    @Query(value = """
            SELECT
                v.via_id AS viaId,
                v.descripcion AS descripcion,
                v.activo AS activo,
                CASE WHEN v.activo = 1 THEN 'Activo' ELSE 'Inactivo' END AS estadoDsc
            FROM via v
            WHERE (:descripcion IS NULL OR :descripcion = ''
                   OR LOWER(v.descripcion) LIKE LOWER(CONCAT('%', :descripcion, '%')))
              AND (:activo IS NULL OR v.activo = :activo)
            ORDER BY v.via_id
            """,
            countQuery = """
                    SELECT COUNT(*)
                    FROM via v
                    WHERE (:descripcion IS NULL OR :descripcion = ''
                           OR LOWER(v.descripcion) LIKE LOWER(CONCAT('%', :descripcion, '%')))
                      AND (:activo IS NULL OR v.activo = :activo)
                    """,
            nativeQuery = true)
    Page<ViaProjection> listarVias(
            @Param("descripcion") String descripcion,
            @Param("activo") Boolean activo,
            Pageable pageable);
}
