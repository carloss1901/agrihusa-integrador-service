package com.proyecto.integrador.repository;

import com.proyecto.integrador.model.entity.DestinoEntity;
import com.proyecto.integrador.model.projection.DestinoProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DestinoRepository extends JpaRepository<DestinoEntity, Integer> {

    boolean existsByPaisIgnoreCaseAndCiudadIgnoreCase(String pais, String ciudad);

    boolean existsByPaisIgnoreCaseAndCiudadIgnoreCaseAndDestinoIdNot(
            String pais, String ciudad, Integer destinoId);

    @Query(value = """
            SELECT
                d.destino_id AS destinoId,
                d.pais AS pais,
                d.ciudad AS ciudad,
                d.activo AS activo,
                CASE WHEN d.activo = 1 THEN 'Activo' ELSE 'Inactivo' END AS estadoDsc
            FROM destino d
            WHERE (:pais IS NULL OR :pais = ''
                   OR LOWER(d.pais) LIKE LOWER(CONCAT('%', :pais, '%')))
              AND (:ciudad IS NULL OR :ciudad = ''
                   OR LOWER(d.ciudad) LIKE LOWER(CONCAT('%', :ciudad, '%')))
              AND (:activo IS NULL OR d.activo = :activo)
            ORDER BY d.destino_id
            """,
            countQuery = """
                    SELECT COUNT(*)
                    FROM destino d
                    WHERE (:pais IS NULL OR :pais = ''
                           OR LOWER(d.pais) LIKE LOWER(CONCAT('%', :pais, '%')))
                      AND (:ciudad IS NULL OR :ciudad = ''
                           OR LOWER(d.ciudad) LIKE LOWER(CONCAT('%', :ciudad, '%')))
                      AND (:activo IS NULL OR d.activo = :activo)
                    """,
            nativeQuery = true)
    Page<DestinoProjection> listarDestinos(
            @Param("pais") String pais,
            @Param("ciudad") String ciudad,
            @Param("activo") Boolean activo,
            Pageable pageable);
}
