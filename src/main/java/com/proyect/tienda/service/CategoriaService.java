package com.proyect.tienda.service;

import com.proyect.tienda.domain.Categoria;
import jdk.dynalink.linker.LinkerServices;

import java.util.List;

public interface CategoriaService {

    public List<Categoria> listarCategoria();

    public  void guardar(Categoria categoria);
}
