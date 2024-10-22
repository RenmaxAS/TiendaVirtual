package com.proyect.tienda.dao;

import com.proyect.tienda.domain.Abono;
import com.proyect.tienda.domain.Venta;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
@Repository
public interface AbonoDao extends JpaRepository<Abono, Long> {

    List<Abono> findByVenta_IdVenta(Long ventaId);

    @Query(value = "SELECT SUM(a.Abono) FROM Abono a WHERE a.abono > 0.00 AND DATE(a.Fecha_Abono) = CURRENT_DATE", nativeQuery = true)
    BigDecimal sumAbonosDelDiaReal();

    @Query(value = "SELECT SUM(a.Abono) as total, DATE(a.Fecha_Abono) as fecha FROM Abono a WHERE a.abono > 0.00 AND DATE(a.Fecha_Abono) BETWEEN DATE_SUB(CURRENT_DATE, INTERVAL 7 DAY) AND CURRENT_DATE GROUP BY DATE(a.Fecha_Abono)", nativeQuery = true)
    List<Map<String, Object>> findGananciasDeLaSemanaReal();

    @Query(value = "SELECT deuda FROM abono WHERE id_venta = :ventaId ORDER BY fecha_abono DESC, id DESC LIMIT 1", nativeQuery = true)
    BigDecimal findUltimaDeudaByVentaId(@Param("ventaId") Long ventaId);
}
