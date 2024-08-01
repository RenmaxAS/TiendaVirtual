package com.proyect.tienda.domain;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "venta")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "idVenta")
public class Venta {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long idVenta;

    @ManyToOne
    @JoinColumn(name = "Id_Cliente", nullable = false)
    private Cliente cliente;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @Column(name = "Fecha_Pedido", nullable = false)
    private Date fechaPedido;

    @Column(name = "Importe_Total", nullable = false)
    private BigDecimal importeTotal;

    @Column(name = "Estado", length = 1)
    private String estado;

    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<VentaDetalle> ventaDetalles = new ArrayList<>();

    public void addVentaDetalle(VentaDetalle ventaDetalle) {
        this.ventaDetalles.add(ventaDetalle);
        ventaDetalle.setVenta(this);
    }

    @JsonIgnore // Evitar la serialización inversa que puede causar ciclos
    @OneToMany(mappedBy = "venta", fetch = FetchType.LAZY)
    private List<Abono> abonos;

}
