package com.proyecto.integrador.repository;

import com.proyecto.integrador.model.entity.DespachoEntity;
import com.proyecto.integrador.model.projection.DespachoProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DespachoRepository extends JpaRepository<DespachoEntity, Integer> {
    boolean existsByCodigoIgnoreCase(String codigo);
    boolean existsByCodigoIgnoreCaseAndDespachoIdNot(String codigo, Integer despachoId);

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
}
