package com.proyect.tienda.service.serviceImpl;

import com.proyect.tienda.dao.ClienteDao;
import com.proyect.tienda.domain.Cliente;
import com.proyect.tienda.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClienteServiceImpl implements ClienteService {

    @Autowired(required = false)
    private ClienteDao clienteDao;

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> listarCliente() {
        return (List<Cliente>) clienteDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> listarClientesPorEstado(String estado) {
        return clienteDao.findByEstado(estado);
    }

    @Override
    @Transactional
    public void guardar(Cliente cliente) {
        clienteDao.save(cliente);
    }

    @Override
    @Transactional
    public void eliminar(Cliente cliente) {
        Cliente data = clienteDao.findById(cliente.getIdCliente()).orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        data.setEstado("I");
        clienteDao.save(data);
    }

    @Override
    @Transactional
    public void reactivar(Cliente cliente) {
        Cliente data = clienteDao.findById(cliente.getIdCliente()).orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        data.setEstado("A");
        clienteDao.save(data);
    }

    @Override
    @Transactional(readOnly = true)
    public Cliente encontrarCliente(Cliente cliente) {
        return clienteDao.findById(cliente.getIdCliente()).orElse(null);
    }

    @Override
    public boolean findByNombreAndApellido(String nombre, String apellido) {
        return clienteDao.findByNombreAndApellido(nombre, apellido) != null;
    }

    @Override
    @Transactional(readOnly = true)
    public Cliente encontrarClientePorId(Long idCliente) {
        return clienteDao.findById(idCliente).orElse(null);
    }

    @Override
    public boolean existeCliente(String nombre, String apellido) {
        return clienteDao.existsByNombreAndApellido(nombre, apellido);
    }

    @Override
    public boolean existeClienteExcluyendoId(Long idCliente, String nombre, String apellido) {
        return clienteDao.existsByNombreAndApellidoAndNotId(nombre, apellido, idCliente);
    }
}
