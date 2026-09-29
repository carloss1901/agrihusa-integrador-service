package com.proyecto.integrador.repository;

import com.proyecto.integrador.model.entity.NavieraEntity;
import com.proyecto.integrador.model.projection.NavieraProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NavieraRepository extends JpaRepository<NavieraEntity, Integer> {
    boolean existsByCodigoIgnoreCase(String codigo);
    boolean existsByNombreIgnoreCase(String nombre);
    boolean existsByCodigoIgnoreCaseAndNavieraIdNot(String codigo, Integer navieraId);
    boolean existsByNombreIgnoreCaseAndNavieraIdNot(String nombre, Integer navieraId);

    @Query(value = """
            SELECT n.naviera_id AS navieraId, n.codigo AS codigo, n.nombre AS nombre,
                   n.pais AS pais, n.contacto AS contacto, n.correo AS correo,
                   n.telefono AS telefono, n.sitio_web AS sitioWeb, n.activo AS activo,
                   CASE WHEN n.activo = 1 THEN 'Activo' ELSE 'Inactivo' END AS estadoDsc
            FROM naviera n
            WHERE (:texto IS NULL OR :texto = ''
                   OR LOWER(n.codigo) LIKE LOWER(CONCAT('%', :texto, '%'))
                   OR LOWER(n.nombre) LIKE LOWER(CONCAT('%', :texto, '%'))
                   OR LOWER(n.contacto) LIKE LOWER(CONCAT('%', :texto, '%')))
              AND (:pais IS NULL OR :pais = '' OR LOWER(n.pais) = LOWER(:pais))
              AND (:activo IS NULL OR n.activo = :activo)
            ORDER BY n.naviera_id
            """, countQuery = """
            SELECT COUNT(*) FROM naviera n
            WHERE (:texto IS NULL OR :texto = ''
                   OR LOWER(n.codigo) LIKE LOWER(CONCAT('%', :texto, '%'))
                   OR LOWER(n.nombre) LIKE LOWER(CONCAT('%', :texto, '%'))
                   OR LOWER(n.contacto) LIKE LOWER(CONCAT('%', :texto, '%')))
              AND (:pais IS NULL OR :pais = '' OR LOWER(n.pais) = LOWER(:pais))
              AND (:activo IS NULL OR n.activo = :activo)
            """, nativeQuery = true)
    Page<NavieraProjection> listarNavieras(@Param("texto") String texto, @Param("pais") String pais,
                                           @Param("activo") Boolean activo, Pageable pageable);
}
