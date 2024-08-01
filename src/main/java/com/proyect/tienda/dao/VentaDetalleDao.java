package com.proyect.tienda.dao;

import com.proyect.tienda.domain.VentaDetalle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VentaDetalleDao extends JpaRepository<VentaDetalle, Long> {
}
