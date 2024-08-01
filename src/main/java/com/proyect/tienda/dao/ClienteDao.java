package com.proyect.tienda.dao;

import com.proyect.tienda.domain.Cliente;
import com.proyect.tienda.domain.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClienteDao extends JpaRepository<Cliente, Long> {

    List<Cliente> findByEstado(String estado);
}
