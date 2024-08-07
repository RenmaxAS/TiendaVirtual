package com.proyect.tienda.service;

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

    @Transactional(readOnly = true)
    Cliente encontrarClientePorId(Long idCliente);
}
