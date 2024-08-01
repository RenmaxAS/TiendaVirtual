package com.proyect.tienda.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "venta_detalle")
public class VentaDetalle {

    private static final long serialversionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long idVentaDetalle;

    @NotNull
    @DecimalMin(value = "0.000", inclusive = false)
    @Column(name = "Cantidad", nullable = false)
    private BigDecimal cantidad;

    @ManyToOne
    @JoinColumn(name = "Id_Venta", nullable = false)
    @ToString.Exclude
    private Venta venta;

    @ManyToOne
    @JoinColumn(name = "Id_Producto", nullable = false)
    private Producto producto;

    @Column(name = "Sub_Total", nullable = false)
    private BigDecimal subTotal;
}
