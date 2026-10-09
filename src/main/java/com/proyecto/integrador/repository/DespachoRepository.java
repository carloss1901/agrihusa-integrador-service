package com.proyecto.integrador.repository;

import com.proyecto.integrador.model.entity.DespachoEntity;
import com.proyecto.integrador.model.projection.DespachoProjection;
import com.proyecto.integrador.model.projection.DespachoResumenProjection;
import com.proyecto.integrador.model.projection.ReporteDespachoProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface DespachoRepository extends JpaRepository<DespachoEntity, Integer> {
    boolean existsByCodigoIgnoreCase(String codigo);
    boolean existsByCodigoIgnoreCaseAndDespachoIdNot(String codigo, Integer despachoId);

    @Query(value = """
            SELECT d.despacho_id AS despachoId, d.codigo AS codigo,
                   d.fecha_despacho AS fechaDespacho, d.fecha_estimada_llegada AS fechaEstimadaLlegada,
                   COALESCE(c.nombre_comercial, c.razon_social) AS cliente,
                   n.nombre AS naviera,
                   CONCAT(de.ciudad, ', ', de.pais) AS destino,
                   COALESCE(o.nombre_comercial, o.razon_social) AS operadorLogistico,
                   CONCAT(pl.puerto, ', ', pl.pais) AS puertoLlegada,
                   p.nombre AS producto, v.nombre AS variedad, vi.descripcion AS via,
                   s.descripcion AS situacion, d.cantidad AS cantidad,
                   d.unidad_medida AS unidadMedida, d.numero_contenedor AS numeroContenedor,
                   d.observaciones AS observaciones, d.activo AS activo
            FROM despacho d
            LEFT JOIN cliente c ON c.cliente_id = d.cliente_id
            LEFT JOIN naviera n ON n.naviera_id = d.naviera_id
            LEFT JOIN destino de ON de.destino_id = d.destino_id
            LEFT JOIN operador_logistico o ON o.operador_logistico_id = d.operador_logistico_id
            LEFT JOIN puerto_llegada pl ON pl.puerto_llegada_id = d.puerto_llegada_id
            LEFT JOIN producto p ON p.producto_id = d.producto_id
            LEFT JOIN variedad v ON v.variedad_id = d.variedad_id
            LEFT JOIN via vi ON vi.via_id = d.via_id
            LEFT JOIN situacion s ON s.situacion_id = d.situacion_id
            WHERE (CAST(:fechaDesde AS DATE) IS NULL OR d.fecha_despacho >= CAST(:fechaDesde AS DATE))
              AND (CAST(:fechaHasta AS DATE) IS NULL OR d.fecha_despacho <= CAST(:fechaHasta AS DATE))
              AND (:clienteId IS NULL OR d.cliente_id = :clienteId)
              AND (:productoId IS NULL OR d.producto_id = :productoId)
              AND (:variedadId IS NULL OR d.variedad_id = :variedadId)
              AND (:viaId IS NULL OR d.via_id = :viaId)
              AND (:situacionId IS NULL OR d.situacion_id = :situacionId)
              AND (:activo IS NULL OR d.activo = :activo)
            ORDER BY d.fecha_despacho DESC, d.despacho_id DESC
            """, nativeQuery = true)
    List<ReporteDespachoProjection> listarReporte(
            @Param("fechaDesde") LocalDate fechaDesde,
            @Param("fechaHasta") LocalDate fechaHasta,
            @Param("clienteId") Integer clienteId,
            @Param("productoId") Integer productoId,
            @Param("variedadId") Integer variedadId,
            @Param("viaId") Integer viaId,
            @Param("situacionId") Integer situacionId,
            @Param("activo") Boolean activo);

    @Query(value = """
            SELECT d.despacho_id AS despachoId, d.codigo AS codigo,
                   d.fecha_despacho AS fechaDespacho, d.fecha_estimada_llegada AS fechaEstimadaLlegada,
                   d.cliente_id AS clienteId, d.naviera_id AS navieraId, d.destino_id AS destinoId,
                   d.operador_logistico_id AS operadorLogisticoId, d.puerto_llegada_id AS puertoLlegadaId,
                   d.producto_id AS productoId, d.variedad_id AS variedadId, d.via_id AS viaId,
                   d.situacion_id AS situacionId, d.cantidad AS cantidad, d.unidad_medida AS unidadMedida,
                   d.numero_contenedor AS numeroContenedor, d.observaciones AS observaciones,
                   d.activo AS activo, CASE WHEN d.activo = 1 THEN 'Activo' ELSE 'Inactivo' END AS estadoDsc
            FROM despacho d
            WHERE (:texto IS NULL OR :texto = ''
                   OR LOWER(d.codigo) LIKE LOWER(CONCAT('%', :texto, '%'))
                   OR LOWER(d.numero_contenedor) LIKE LOWER(CONCAT('%', :texto, '%'))
                   OR LOWER(d.unidad_medida) LIKE LOWER(CONCAT('%', :texto, '%')))
              AND (:clienteId IS NULL OR d.cliente_id = :clienteId)
              AND (:situacionId IS NULL OR d.situacion_id = :situacionId)
              AND (:activo IS NULL OR d.activo = :activo)
            ORDER BY d.despacho_id DESC
            """, countQuery = """
            SELECT COUNT(*) FROM despacho d
            WHERE (:texto IS NULL OR :texto = ''
                   OR LOWER(d.codigo) LIKE LOWER(CONCAT('%', :texto, '%'))
                   OR LOWER(d.numero_contenedor) LIKE LOWER(CONCAT('%', :texto, '%'))
                   OR LOWER(d.unidad_medida) LIKE LOWER(CONCAT('%', :texto, '%')))
              AND (:clienteId IS NULL OR d.cliente_id = :clienteId)
              AND (:situacionId IS NULL OR d.situacion_id = :situacionId)
              AND (:activo IS NULL OR d.activo = :activo)
            """, nativeQuery = true)
    Page<DespachoProjection> listarDespachos(@Param("texto") String texto,
                                             @Param("clienteId") Integer clienteId,
                                             @Param("situacionId") Integer situacionId,
                                             @Param("activo") Boolean activo,
                                             Pageable pageable);

    @Query(value = """
            SELECT MAX(TRY_CAST(SUBSTRING(d.codigo, LEN(:prefijo) + 1, 10) AS INT))
            FROM despacho d WITH (UPDLOCK, HOLDLOCK)
            WHERE d.codigo LIKE CONCAT(:prefijo, '%')
            """, nativeQuery = true)
    Integer obtenerUltimoCorrelativo(@Param("prefijo") String prefijo);

    @Query(value = """
            SELECT d.unidad_medida AS unidadMedida,
                   CAST(COUNT(*) AS BIGINT) AS totalDespachos,
                   SUM(d.cantidad) AS cantidadTotal
            FROM despacho d
            WHERE (CAST(:fechaDesde AS DATE) IS NULL OR d.fecha_despacho >= CAST(:fechaDesde AS DATE))
              AND (CAST(:fechaHasta AS DATE) IS NULL OR d.fecha_despacho <= CAST(:fechaHasta AS DATE))
              AND (:clienteId IS NULL OR d.cliente_id = :clienteId)
              AND (:productoId IS NULL OR d.producto_id = :productoId)
              AND (:variedadId IS NULL OR d.variedad_id = :variedadId)
              AND (:viaId IS NULL OR d.via_id = :viaId)
              AND (:situacionId IS NULL OR d.situacion_id = :situacionId)
              AND (:activo IS NULL OR d.activo = :activo)
            GROUP BY d.unidad_medida
            ORDER BY d.unidad_medida
            """, nativeQuery = true)
    List<DespachoResumenProjection> resumenDespachos(
            @Param("fechaDesde") LocalDate fechaDesde,
            @Param("fechaHasta") LocalDate fechaHasta,
            @Param("clienteId") Integer clienteId,
            @Param("productoId") Integer productoId,
            @Param("variedadId") Integer variedadId,
            @Param("viaId") Integer viaId,
            @Param("situacionId") Integer situacionId,
            @Param("activo") Boolean activo);
}
