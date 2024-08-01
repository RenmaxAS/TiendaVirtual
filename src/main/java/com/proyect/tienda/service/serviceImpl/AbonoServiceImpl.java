package com.proyect.tienda.service.serviceImpl;

import com.proyect.tienda.dao.AbonoDao;
import com.proyect.tienda.domain.Abono;
import com.proyect.tienda.domain.Venta;
import com.proyect.tienda.service.AbonoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AbonoServiceImpl implements AbonoService {

    @Autowired(required=false)
    private AbonoDao abonoDao;

    @Override
    public List<Abono> obtenerAbonosPorVenta(Long ventaId) {
        return abonoDao.findByVenta_IdVenta(ventaId); // Utiliza el método del DAO para obtener abonos por ventaId
    }

    public BigDecimal findUltimaDeudaByVentaId(Long idVenta) {
        return abonoDao.findUltimaDeudaByVentaId(idVenta);
    }
}
