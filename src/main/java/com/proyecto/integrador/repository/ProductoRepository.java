package com.proyecto.integrador.repository;

import com.proyecto.integrador.model.entity.ProductoEntity;
import com.proyecto.integrador.model.projection.ProductoProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductoRepository extends JpaRepository<ProductoEntity, Integer> {

    java.util.List<ProductoEntity> findAllByActivoTrueOrderByNombreAsc();

    boolean existsByCodigoIgnoreCase(String codigo);

    boolean existsByNombreIgnoreCase(String nombre);

    boolean existsByCodigoIgnoreCaseAndProductoIdNot(String codigo, Integer productoId);

    boolean existsByNombreIgnoreCaseAndProductoIdNot(String nombre, Integer productoId);

    @Query(value = """
            SELECT
                p.producto_id AS productoId,
                p.codigo AS codigo,
                p.nombre AS nombre,
                p.descripcion AS descripcion,
                p.activo AS activo,
                CASE WHEN p.activo = 1 THEN 'Activo' ELSE 'Inactivo' END AS estadoDsc
            FROM producto p
            WHERE (:texto IS NULL OR :texto = ''
                   OR LOWER(p.codigo) LIKE LOWER(CONCAT('%', :texto, '%'))
                   OR LOWER(p.nombre) LIKE LOWER(CONCAT('%', :texto, '%'))
                   OR LOWER(p.descripcion) LIKE LOWER(CONCAT('%', :texto, '%')))
              AND (:activo IS NULL OR p.activo = :activo)
            ORDER BY p.producto_id
            """,
            countQuery = """
                    SELECT COUNT(*)
                    FROM producto p
                    WHERE (:texto IS NULL OR :texto = ''
                           OR LOWER(p.codigo) LIKE LOWER(CONCAT('%', :texto, '%'))
                           OR LOWER(p.nombre) LIKE LOWER(CONCAT('%', :texto, '%'))
                           OR LOWER(p.descripcion) LIKE LOWER(CONCAT('%', :texto, '%')))
                      AND (:activo IS NULL OR p.activo = :activo)
                    """,
            nativeQuery = true)
    Page<ProductoProjection> listarProductos(
            @Param("texto") String texto,
            @Param("activo") Boolean activo,
            Pageable pageable);
}
