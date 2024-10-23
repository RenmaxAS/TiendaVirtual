package com.proyect.tienda.service;

import com.proyect.tienda.domain.Categories;
import com.proyect.tienda.domain.Cliente;
import com.proyect.tienda.domain.Producto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ClienteService {

    public List<Cliente> listarCliente();

    public List<Cliente> listarClientesPorEstado(String estado);

    public void guardar(Cliente cliente);

    public void eliminar(Cliente cliente);
    public void reactivar(Cliente cliente);

    public Cliente encontrarCliente(Cliente cliente);

    boolean findByNombreAndApellido(String nombre, String apellido);

    @Transactional(readOnly = true)
    Cliente encontrarClientePorId(Long idCliente);

    public boolean existeCliente(String nombre, String apellido);

    boolean existeClienteExcluyendoId(Long idCliente, String nombre, String apellido);
}
