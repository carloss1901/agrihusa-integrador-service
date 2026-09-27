package com.proyecto.integrador.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "persona", schema = "public")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersonaEntity {

    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "nombres")
    private String nombres;
}
