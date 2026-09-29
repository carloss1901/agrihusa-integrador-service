package com.proyecto.integrador.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "bitacora")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BitacoraEntity extends AuditoriaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bitacora_id")
    private Integer bitacoraId;

    @Column(name = "fecha")
    private LocalDateTime fecha;

    @Column(name = "usuario_id")
    private Integer usuarioId;

    @Column(name = "modulo")
    private String modulo;

    @Column(name = "accion")
    private String accion;

    @Column(name = "entidad")
    private String entidad;

    @Column(name = "registro_id")
    private Integer registroId;

    @Column(name = "detalle")
    private String detalle;

    @Column(name = "resultado")
    private String resultado;

    @Column(name = "activo")
    private Boolean activo;
}
