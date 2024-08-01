package com.proyect.tienda.service;

import com.proyect.tienda.domain.Abono;
import com.proyect.tienda.domain.Venta;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public interface AbonoService {
    List<Abono> obtenerAbonosPorVenta(Long ventaId);

    public BigDecimal findUltimaDeudaByVentaId(Long idVenta);

}
