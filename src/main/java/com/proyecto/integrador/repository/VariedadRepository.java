package com.proyecto.integrador.repository;

import com.proyecto.integrador.model.entity.VariedadEntity;
import com.proyecto.integrador.model.projection.VariedadProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VariedadRepository extends JpaRepository<VariedadEntity, Integer> {

    boolean existsByProductoIdAndNombreIgnoreCase(Integer productoId, String nombre);

    boolean existsByProductoIdAndNombreIgnoreCaseAndVariedadIdNot(
            Integer productoId, String nombre, Integer variedadId);

    @Query(value = """
            SELECT
                v.variedad_id AS variedadId,
                v.producto_id AS productoId,
                v.nombre AS nombre,
                v.activo AS activo,
                CASE WHEN v.activo = 1 THEN 'Activo' ELSE 'Inactivo' END AS estadoDsc
            FROM variedad v
            WHERE (:texto IS NULL OR :texto = ''
                   OR LOWER(v.nombre) LIKE LOWER(CONCAT('%', :texto, '%')))
              AND (:productoId IS NULL OR v.producto_id = :productoId)
              AND (:activo IS NULL OR v.activo = :activo)
            ORDER BY v.producto_id, v.variedad_id
            """,
            countQuery = """
                    SELECT COUNT(*)
                    FROM variedad v
                    WHERE (:texto IS NULL OR :texto = ''
                           OR LOWER(v.nombre) LIKE LOWER(CONCAT('%', :texto, '%')))
                      AND (:productoId IS NULL OR v.producto_id = :productoId)
                      AND (:activo IS NULL OR v.activo = :activo)
                    """,
            nativeQuery = true)
    Page<VariedadProjection> listarVariedades(
            @Param("texto") String texto,
            @Param("productoId") Integer productoId,
            @Param("activo") Boolean activo,
            Pageable pageable);
}
