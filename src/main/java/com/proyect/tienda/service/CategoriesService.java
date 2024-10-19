package com.proyect.tienda.service;


import com.proyect.tienda.domain.Categories;

import java.util.List;

public interface CategoriesService {

    public List<Categories> listarCategoria();

    public void guardarCategoria(Categories categories);

    public void eliminarCategoria(Categories categories);

    public Categories encontrarCategoria(Categories categories);

    public Categories encontrarCategoriaPorId(Long id);

    public boolean existeCategotia(String categoria);
}
