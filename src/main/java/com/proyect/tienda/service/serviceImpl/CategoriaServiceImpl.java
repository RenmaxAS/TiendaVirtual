package com.proyect.tienda.service.serviceImpl;

import com.proyect.tienda.dao.CategoriaDao;
import com.proyect.tienda.domain.Categoria;
import com.proyect.tienda.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class CategoriaServiceImpl implements CategoriaService {

    @Autowired
    private CategoriaDao categoriaDao;


    @Override
    @Transactional(readOnly = true)
    public List<Categoria> listarCategoria() {
        return (List<Categoria>) categoriaDao.findAll();
    }

    @Override
    public void guardar(Categoria categoria) {
        categoriaDao.save(categoria);
    }
}
