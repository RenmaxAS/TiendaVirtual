package com.proyect.tienda.service;

import com.proyect.tienda.domain.Venta;

import java.util.Date;
import java.util.List;

public interface VentaService {

    public Venta crearVenta(Venta venta);

    List<Venta> findVentasByClienteAndFecha(Long clienteId, Date fechaDesde, Date fechaHasta);

    Venta obtenerVentaPorId(Long idVenta);
}
