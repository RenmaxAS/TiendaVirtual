package com.proyect.tienda.dao;

import com.proyect.tienda.domain.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface VentaDao extends JpaRepository<Venta, Long> {
    List<Venta> findByCliente_IdClienteAndFechaPedidoBetween(Long clienteId, Date fechaDesde, Date fechaHasta);

    List<Venta> findByCliente_IdClienteAndEstado(int clienteId, String estado);

    Venta findByIdVenta(Long idVenta);

    @Query(value = "SELECT SUM(v.Importe_Total) FROM Venta v WHERE v.Importe_Total > 0.00 AND DATE(v.Fecha_Pedido) = CURRENT_DATE", nativeQuery = true)
    BigDecimal sumAbonosDelDiaTotal();

    @Query(value = "SELECT SUM(v.Importe_Total) as total, DATE(v.Fecha_Pedido) as fecha FROM Venta v WHERE v.Importe_Total > 0.00 AND DATE(v.Fecha_Pedido) BETWEEN DATE_SUB(CURRENT_DATE, INTERVAL 7 DAY) AND CURRENT_DATE GROUP BY DATE(v.Fecha_Pedido)", nativeQuery = true)
    List<Map<String, Object>> findGananciasDeLaSemanaTotal();
}
