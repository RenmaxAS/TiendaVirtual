package com.proyect.tienda.service.serviceImpl;

import com.proyect.tienda.dao.ProductoDao;
import com.proyect.tienda.domain.Producto;
import com.proyect.tienda.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductoServiceImpl implements ProductoService {

    @Autowired(required = false)
    private ProductoDao productoDao;

    @Override
    @Transactional(readOnly = true)
    public List<Producto> listarProducto() {
        return (List<Producto>) productoDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Producto> listarProductosPorEstado(String estado) {
        return productoDao.findByEstado(estado);
    }

    @Override
    public List<Producto> listarProductosPorVencer() {
        return productoDao.findProductosPorVencer();
    }

    @Override
    public List<Producto> listarProductosVencidos() {
        return productoDao.findProductosVencidos();
    }

    @Override
    public void guardar(Producto producto) {
        productoDao.save(producto);
    }

    @Override
    @Transactional
    public void eliminar(Producto producto) {
        Producto data = productoDao.findById(producto.getIdProducto()).orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        data.setEstado("I");
        productoDao.save(data);
    }

    @Override
    @Transactional
    public void reactivar(Producto producto) {
        Producto data = productoDao.findById(producto.getIdProducto()).orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        data.setEstado("A");
        productoDao.save(data);
    }

    @Override
    @Transactional(readOnly = true)
    public Producto encontrarProducto(Producto producto) {
        return productoDao.findById(producto.getIdProducto()).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public Producto encontrarProductoPorId(Long idProducto) {
        return productoDao.findById(idProducto).orElse(null);
    }

    public boolean existeProducto(String nombre, String marca, String formato) {
        return productoDao.existsByNombreAndMarcaAndFormato(nombre, marca, formato);
    }

    public boolean existeProductoExcluyendoId(Long id, String nombre, String marca, String formato) {
        return productoDao.existsByNombreAndMarcaAndFormatoAndNotId(nombre, marca, formato, id);
    }


}
