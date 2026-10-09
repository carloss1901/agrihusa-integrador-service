package com.proyecto.integrador.repository;

import com.proyecto.integrador.model.entity.OperadorLogisticoEntity;
import com.proyecto.integrador.model.projection.OperadorLogisticoProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OperadorLogisticoRepository extends JpaRepository<OperadorLogisticoEntity, Integer> {

    boolean existsByRucIgnoreCase(String ruc);

    boolean existsByRazonSocialIgnoreCase(String razonSocial);

    boolean existsByRucIgnoreCaseAndOperadorLogisticoIdNot(String ruc, Integer operadorLogisticoId);

    boolean existsByRazonSocialIgnoreCaseAndOperadorLogisticoIdNot(
            String razonSocial, Integer operadorLogisticoId);

    @Query(value = """
            SELECT
                o.operador_logistico_id AS operadorLogisticoId,
                o.ruc AS ruc,
                o.razon_social AS razonSocial,
                o.nombre_comercial AS nombreComercial,
                o.contacto AS contacto,
                o.correo AS correo,
                o.telefono AS telefono,
                o.direccion AS direccion,
                o.activo AS activo,
                CASE WHEN o.activo = 1 THEN 'Activo' ELSE 'Inactivo' END AS estadoDsc
            FROM operador_logistico o
            WHERE (:texto IS NULL OR :texto = ''
                   OR LOWER(o.ruc) LIKE LOWER(CONCAT('%', :texto, '%'))
                   OR LOWER(o.razon_social) LIKE LOWER(CONCAT('%', :texto, '%'))
                   OR LOWER(o.nombre_comercial) LIKE LOWER(CONCAT('%', :texto, '%'))
                   OR LOWER(o.contacto) LIKE LOWER(CONCAT('%', :texto, '%')))
              AND (:activo IS NULL OR o.activo = :activo)
            ORDER BY o.operador_logistico_id
            """,
            countQuery = """
                    SELECT COUNT(*)
                    FROM operador_logistico o
                    WHERE (:texto IS NULL OR :texto = ''
                           OR LOWER(o.ruc) LIKE LOWER(CONCAT('%', :texto, '%'))
                           OR LOWER(o.razon_social) LIKE LOWER(CONCAT('%', :texto, '%'))
                           OR LOWER(o.nombre_comercial) LIKE LOWER(CONCAT('%', :texto, '%'))
                           OR LOWER(o.contacto) LIKE LOWER(CONCAT('%', :texto, '%')))
                      AND (:activo IS NULL OR o.activo = :activo)
                    """,
            nativeQuery = true)
    Page<OperadorLogisticoProjection> listarOperadores(
            @Param("texto") String texto,
            @Param("activo") Boolean activo,
            Pageable pageable);
}
