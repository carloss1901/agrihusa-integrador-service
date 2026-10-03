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

@Entity
@Table(name = "rol_permiso")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RolPermisoEntity extends AuditoriaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rol_permiso_id")
    private Integer rolPermisoId;

    @Column(name = "rol_id")
    private Integer rolId;

    @Column(name = "modulo_id")
    private Integer moduloId;

    @Column(name = "permiso_id")
    private Integer permisoId;

    @Column(name = "activo")
    private Boolean activo;
}
