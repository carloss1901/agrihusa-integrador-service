package com.proyecto.integrador.repository;

import com.proyecto.integrador.model.entity.ClienteEntity;
import com.proyecto.integrador.model.projection.ClienteProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ClienteRepository extends JpaRepository<ClienteEntity, Integer> {
    java.util.List<ClienteEntity> findAllByActivoTrueOrderByRazonSocialAsc();
    boolean existsByNumeroDocumentoIgnoreCase(String numeroDocumento);
    boolean existsByRazonSocialIgnoreCase(String razonSocial);
    boolean existsByNumeroDocumentoIgnoreCaseAndClienteIdNot(String numeroDocumento, Integer clienteId);
    boolean existsByRazonSocialIgnoreCaseAndClienteIdNot(String razonSocial, Integer clienteId);

    @Query(value = """
            SELECT c.cliente_id AS clienteId, c.tipo_documento AS tipoDocumento, c.numero_documento AS numeroDocumento,
                   c.razon_social AS razonSocial, c.nombre_comercial AS nombreComercial, c.contacto AS contacto,
                   c.correo AS correo, c.telefono AS telefono, c.direccion AS direccion, c.pais AS pais,
                   c.activo AS activo, CASE WHEN c.activo = 1 THEN 'Activo' ELSE 'Inactivo' END AS estadoDsc
            FROM cliente c
            WHERE (:texto IS NULL OR :texto = ''
                   OR LOWER(c.numero_documento) LIKE LOWER(CONCAT('%', :texto, '%'))
                   OR LOWER(c.razon_social) LIKE LOWER(CONCAT('%', :texto, '%'))
                   OR LOWER(c.nombre_comercial) LIKE LOWER(CONCAT('%', :texto, '%'))
                   OR LOWER(c.contacto) LIKE LOWER(CONCAT('%', :texto, '%')))
              AND (:tipoDocumento IS NULL OR :tipoDocumento = '' OR LOWER(c.tipo_documento) = LOWER(:tipoDocumento))
              AND (:activo IS NULL OR c.activo = :activo)
            ORDER BY c.cliente_id
            """, countQuery = """
            SELECT COUNT(*) FROM cliente c
            WHERE (:texto IS NULL OR :texto = ''
                   OR LOWER(c.numero_documento) LIKE LOWER(CONCAT('%', :texto, '%'))
                   OR LOWER(c.razon_social) LIKE LOWER(CONCAT('%', :texto, '%'))
                   OR LOWER(c.nombre_comercial) LIKE LOWER(CONCAT('%', :texto, '%'))
                   OR LOWER(c.contacto) LIKE LOWER(CONCAT('%', :texto, '%')))
              AND (:tipoDocumento IS NULL OR :tipoDocumento = '' OR LOWER(c.tipo_documento) = LOWER(:tipoDocumento))
              AND (:activo IS NULL OR c.activo = :activo)
            """, nativeQuery = true)
    Page<ClienteProjection> listarClientes(@Param("texto") String texto, @Param("tipoDocumento") String tipoDocumento,
                                           @Param("activo") Boolean activo, Pageable pageable);
}
