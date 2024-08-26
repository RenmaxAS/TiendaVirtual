package com.proyect.tienda.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.io.Serializable;

@Data
@Entity
@Table(name = "categoria")
public class Categoria implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long idCategoria;

    @NotEmpty
    @Column(name = "Categoria")
    private String categoria;

    @Column(name = "Descripcion")
    private String descripcion;
}
