package com.proyect.tienda.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "producto")
public class Producto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long idProducto;

    private Long idCategoria;

    @NotEmpty
    @Column(name = "Nombre")
    private String nombre;

    @NotEmpty
    @Column(name = "Marca")
    private String marca;

    @NotEmpty
    @Column(name = "Formato")
    private String formato;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    @Column(name = "Costo")
    private BigDecimal Costo;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    @Column(name = "Precio")
    private BigDecimal precio;

    @NotNull
    @DecimalMin(value = "0.000", inclusive = true)
    @Column(name = "Stock")
    private BigDecimal stock;

    @Lob
    @Column(name = "Imagen")
    private String imagen;

    @NotNull
    @Column(name = "Fecha_Vencimiento", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy") // Formato de fecha esperado desde la vista
    private LocalDate fechaVencimiento;

    @NotEmpty
    @Column(name = "Estado")
    private String estado;

}
