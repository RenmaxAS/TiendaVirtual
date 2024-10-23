package com.proyect.tienda.dao;

import com.proyect.tienda.domain.Categories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriesDao extends JpaRepository<Categories, Long> {

    Categories findByCategoria(String categoria);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Categories c WHERE c.categoria = :categoria")
    boolean existsByCategoria(@Param("categoria") String categoria);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Categories c WHERE c.categoria = :categoria AND c.idCategoria <> :id")
    boolean existsByCategoriaNotId(String categoria, Long id);
}
