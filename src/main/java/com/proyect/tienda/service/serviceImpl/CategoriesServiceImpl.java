package com.proyect.tienda.service.serviceImpl;

import com.proyect.tienda.dao.CategoriesDao;
import com.proyect.tienda.domain.Categories;
import com.proyect.tienda.service.CategoriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriesServiceImpl implements CategoriesService {

    @Autowired
    private CategoriesDao categoriesDao;

    @Override
    public List<Categories> listarCategoria() {
        return categoriesDao.findAll();
    }

    @Override
    public void guardarCategoria(Categories categories) {
        categoriesDao.save(categories);
    }

    @Override
    public void eliminarCategoria(Categories categories) {
        categoriesDao.delete(categories);
    }

    @Override
    public Categories encontrarCategoria(Categories categories) {
        return categoriesDao.findById(categories.getId()).orElse(null);
    }

    @Override
    public Categories encontrarCategoriaPorId(Long id) {
        return categoriesDao.findById(id).orElse(null);
    }

    @Override
    public boolean existeCategotia(String categoria) {
        return categoriesDao.findByCategoria(categoria) != null;
    }
}
