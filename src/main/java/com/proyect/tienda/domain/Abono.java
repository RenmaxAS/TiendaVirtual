package com.proyect.tienda.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.DatabindException;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
@Table(name = "abono")
public class Abono {

    private static final long SerialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long idAbono;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "Id_Venta", nullable = false)
    @JsonBackReference
    private Venta venta;

    @Column(name = "Abono", nullable = false)
    private BigDecimal abono;

    @Column(name = "Deuda", nullable = false)
    private BigDecimal deuda;

    @NotNull
    @Column(name = "Fecha_Abono", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date fechaAbono;
}
