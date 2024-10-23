package com.proyect.tienda.dao;

import com.proyect.tienda.domain.Cliente;
import com.proyect.tienda.domain.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClienteDao extends JpaRepository<Cliente, Long> {

    List<Cliente> findByEstado(String estado);

    Cliente findByNombreAndApellido(String nombre, String apellido);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Cliente c WHERE c.nombre = :nombre AND c.apellido = :apellido")
    boolean existsByNombreAndApellido(@Param("nombre") String nombre, @Param("apellido") String apellido);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Cliente c WHERE c.nombre = :nombre AND c.apellido = :apellido AND c.idCliente <> :id")
    boolean existsByNombreAndApellidoAndNotId(String nombre, String apellido, Long id);
}
