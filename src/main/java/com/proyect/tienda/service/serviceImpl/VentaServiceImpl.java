package com.proyect.tienda.service.serviceImpl;

import com.proyect.tienda.dao.ProductoDao;
import com.proyect.tienda.dao.VentaDao;
import com.proyect.tienda.dao.VentaDetalleDao;
import com.proyect.tienda.domain.Producto;
import com.proyect.tienda.domain.Venta;
import com.proyect.tienda.domain.VentaDetalle;
import com.proyect.tienda.service.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class VentaServiceImpl implements VentaService {

    @Autowired(required = false)
    private VentaDao ventaDao;

    @Autowired(required = false)
    private VentaDetalleDao ventaDetalleDao;

    @Autowired(required = false)
    private ProductoDao productoDao;

    @Transactional
    public Venta crearVenta(Venta venta) {
        Venta nuevaVenta = ventaDao.save(venta);

        for (VentaDetalle detalle : venta.getVentaDetalles()){
            Producto producto = productoDao.findById(detalle.getProducto().getIdProducto())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            BigDecimal nuevoStock = producto.getStock().subtract(detalle.getCantidad());
            if (nuevoStock.compareTo(BigDecimal.ZERO) < 0) {
                throw new RuntimeException("Stock insuficiente para el producto: " + producto.getNombre() + ' ' + producto.getMarca() + ' ' + producto.getFormato());
            }
            producto.setStock(nuevoStock);
            productoDao.save(producto);
        }
        return nuevaVenta;
    }

    public List<Venta> findVentasByClienteAndFecha(Long clienteId, Date fechaDesde, Date fechaHasta) {
        return ventaDao.findByCliente_IdClienteAndFechaPedidoBetween(clienteId, fechaDesde, fechaHasta);
    }

    public Venta obtenerVentaPorId(Long idVenta) {
        Optional<Venta> venta = ventaDao.findById(idVenta);
        return venta.orElse(null); // Devuelve null si la venta no se encuentra
    }
}
