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

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "despacho")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DespachoEntity extends AuditoriaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "despacho_id")
    private Integer despachoId;

    @Column(name = "codigo")
    private String codigo;

    @Column(name = "fecha_despacho")
    private LocalDate fechaDespacho;

    @Column(name = "fecha_estimada_llegada")
    private LocalDate fechaEstimadaLlegada;

    @Column(name = "cliente_id")
    private Integer clienteId;

    @Column(name = "naviera_id")
    private Integer navieraId;

    @Column(name = "destino_id")
    private Integer destinoId;

    @Column(name = "operador_logistico_id")
    private Integer operadorLogisticoId;

    @Column(name = "puerto_llegada_id")
    private Integer puertoLlegadaId;

    @Column(name = "producto_id")
    private Integer productoId;

    @Column(name = "variedad_id")
    private Integer variedadId;

    @Column(name = "via_id")
    private Integer viaId;

    @Column(name = "situacion_id")
    private Integer situacionId;

    @Column(name = "cantidad")
    private BigDecimal cantidad;

    @Column(name = "unidad_medida")
    private String unidadMedida;

    @Column(name = "numero_contenedor")
    private String numeroContenedor;

    @Column(name = "observaciones")
    private String observaciones;

    @Column(name = "activo")
    private Boolean activo;
}
