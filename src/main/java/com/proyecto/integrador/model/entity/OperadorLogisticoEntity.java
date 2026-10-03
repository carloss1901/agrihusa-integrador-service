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
@Table(name = "operador_logistico")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OperadorLogisticoEntity extends AuditoriaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "operador_logistico_id")
    private Integer operadorLogisticoId;

    @Column(name = "ruc")
    private String ruc;

    @Column(name = "razon_social")
    private String razonSocial;

    @Column(name = "nombre_comercial")
    private String nombreComercial;

    @Column(name = "contacto")
    private String contacto;

    @Column(name = "correo")
    private String correo;

    @Column(name = "telefono")
    private String telefono;

    @Column(name = "direccion")
    private String direccion;

    @Column(name = "activo")
    private Boolean activo;
}
