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
@Table(name = "puerto_llegada")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PuertoLlegadaEntity extends AuditoriaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "puerto_llegada_id")
    private Integer puertoLlegadaId;

    @Column(name = "codigo")
    private String codigo;

    @Column(name = "puerto")
    private String puerto;

    @Column(name = "pais")
    private String pais;

    @Column(name = "activo")
    private Boolean activo;
}
