package com.proyecto.integrador.repository;

import com.proyecto.integrador.model.entity.BitacoraEntity;
import com.proyecto.integrador.model.projection.BitacoraProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface BitacoraRepository
        extends JpaRepository<BitacoraEntity, Integer> {

    @Query(
            value = """
            SELECT
                b.bitacora_id AS bitacoraId,
                b.fecha AS fecha,
                b.usuario_id AS usuarioId,
                COALESCE(u.usuario, 'SISTEMA') AS nombreUsuario,
                b.modulo AS modulo,
                b.accion AS accion,
                b.entidad AS entidad,
                b.registro_id AS registroId,
                b.detalle AS detalle,
                b.resultado AS resultado,
                b.activo AS activo,
                b.fecha_creacion AS fechaCreacion,
                b.fecha_modificacion AS fechaModificacion
            FROM bitacora b
            LEFT JOIN usuario u
                ON u.usuario_id = b.usuario_id
            WHERE (
                :usuario IS NULL
                OR :usuario = ''
                OR UPPER(u.usuario) LIKE
                    UPPER(CONCAT('%', :usuario, '%'))
            )
              AND (
                :modulo IS NULL
                OR :modulo = ''
                OR LOWER(b.modulo) LIKE
                    LOWER(CONCAT('%', :modulo, '%'))
              )
              AND (
                :accion IS NULL
                OR :accion = ''
                OR LOWER(b.accion) LIKE
                    LOWER(CONCAT('%', :accion, '%'))
              )
              AND (
                :entidad IS NULL
                OR :entidad = ''
                OR LOWER(b.entidad) LIKE
                    LOWER(CONCAT('%', :entidad, '%'))
              )
              AND (
                :resultado IS NULL
                OR :resultado = ''
                OR LOWER(b.resultado) LIKE
                    LOWER(CONCAT('%', :resultado, '%'))
              )
              AND (:activo IS NULL OR b.activo = :activo)
              AND (
                :fechaDesde IS NULL
                OR b.fecha >= :fechaDesde
              )
              AND (
                :fechaHasta IS NULL
                OR b.fecha <= :fechaHasta
              )
            ORDER BY b.bitacora_id DESC
            """,
            countQuery = """
            SELECT COUNT(*)
            FROM bitacora b
            LEFT JOIN usuario u
                ON u.usuario_id = b.usuario_id
            WHERE (
                :usuario IS NULL
                OR :usuario = ''
                OR UPPER(u.usuario) LIKE
                    UPPER(CONCAT('%', :usuario, '%'))
            )
              AND (
                :modulo IS NULL
                OR :modulo = ''
                OR LOWER(b.modulo) LIKE
                    LOWER(CONCAT('%', :modulo, '%'))
              )
              AND (
                :accion IS NULL
                OR :accion = ''
                OR LOWER(b.accion) LIKE
                    LOWER(CONCAT('%', :accion, '%'))
              )
              AND (
                :entidad IS NULL
                OR :entidad = ''
                OR LOWER(b.entidad) LIKE
                    LOWER(CONCAT('%', :entidad, '%'))
              )
              AND (
                :resultado IS NULL
                OR :resultado = ''
                OR LOWER(b.resultado) LIKE
                    LOWER(CONCAT('%', :resultado, '%'))
              )
              AND (:activo IS NULL OR b.activo = :activo)
              AND (
                :fechaDesde IS NULL
                OR b.fecha >= :fechaDesde
              )
              AND (
                :fechaHasta IS NULL
                OR b.fecha <= :fechaHasta
              )
            """,
            nativeQuery = true
    )
    Page<BitacoraProjection> listarBitacoras(
            @Param("usuario") String usuario,
            @Param("modulo") String modulo,
            @Param("accion") String accion,
            @Param("entidad") String entidad,
            @Param("resultado") String resultado,
            @Param("activo") Boolean activo,
            @Param("fechaDesde") LocalDateTime fechaDesde,
            @Param("fechaHasta") LocalDateTime fechaHasta,
            Pageable pageable
    );
}