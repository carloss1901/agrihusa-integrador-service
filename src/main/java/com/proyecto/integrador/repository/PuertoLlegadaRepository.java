package com.proyecto.integrador.repository;

import com.proyecto.integrador.model.entity.PuertoLlegadaEntity;
import com.proyecto.integrador.model.projection.PuertoLlegadaProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PuertoLlegadaRepository extends JpaRepository<PuertoLlegadaEntity, Integer> {
    java.util.List<PuertoLlegadaEntity> findAllByActivoTrueOrderByPuertoAsc();

    boolean existsByCodigoIgnoreCase(String codigo);

    boolean existsByPuertoIgnoreCaseAndPaisIgnoreCase(String puerto, String pais);

    boolean existsByCodigoIgnoreCaseAndPuertoLlegadaIdNot(String codigo, Integer puertoLlegadaId);

    boolean existsByPuertoIgnoreCaseAndPaisIgnoreCaseAndPuertoLlegadaIdNot(
            String puerto, String pais, Integer puertoLlegadaId);

    @Query(value = """
            SELECT
                p.puerto_llegada_id AS puertoLlegadaId,
                p.codigo AS codigo,
                p.puerto AS puerto,
                p.pais AS pais,
                p.activo AS activo,
                CASE WHEN p.activo = 1 THEN 'Activo' ELSE 'Inactivo' END AS estadoDsc
            FROM puerto_llegada p
            WHERE (:texto IS NULL OR :texto = ''
                   OR LOWER(p.codigo) LIKE LOWER(CONCAT('%', :texto, '%'))
                   OR LOWER(p.puerto) LIKE LOWER(CONCAT('%', :texto, '%'))
                   OR LOWER(p.pais) LIKE LOWER(CONCAT('%', :texto, '%')))
              AND (:pais IS NULL OR :pais = '' OR LOWER(p.pais) = LOWER(:pais))
              AND (:activo IS NULL OR p.activo = :activo)
            ORDER BY p.puerto_llegada_id
            """,
            countQuery = """
                    SELECT COUNT(*)
                    FROM puerto_llegada p
                    WHERE (:texto IS NULL OR :texto = ''
                           OR LOWER(p.codigo) LIKE LOWER(CONCAT('%', :texto, '%'))
                           OR LOWER(p.puerto) LIKE LOWER(CONCAT('%', :texto, '%'))
                           OR LOWER(p.pais) LIKE LOWER(CONCAT('%', :texto, '%')))
                      AND (:pais IS NULL OR :pais = '' OR LOWER(p.pais) = LOWER(:pais))
                      AND (:activo IS NULL OR p.activo = :activo)
                    """,
            nativeQuery = true)
    Page<PuertoLlegadaProjection> listarPuertos(
            @Param("texto") String texto,
            @Param("pais") String pais,
            @Param("activo") Boolean activo,
            Pageable pageable);
}
