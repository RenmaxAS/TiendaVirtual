package com.proyect.tienda.service;

import com.proyect.tienda.domain.Cliente;
import com.proyect.tienda.domain.Producto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProductoService {

    public List<Producto> listarProducto();

    public List<Producto> listarProductosPorEstado(String estado);

    // Nuevo método para listar productos por vencer
    public List<Producto> listarProductosPorVencer();

    public List<Producto> listarProductosVencidos();

    public void guardar(Producto producto);

    public void eliminar(Producto producto);

    public void reactivar(Producto producto);

    public Producto encontrarProducto(Producto producto);

    @Transactional(readOnly = true)
    Producto encontrarProductoPorId(Long idProducto);

    public boolean existeProducto(String nombre, String marca, String formato);

    boolean existeProductoExcluyendoId(Long idProducto, String nombre, String marca, String formato);
}
