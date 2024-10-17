package com.proyect.tienda.dao;

import com.proyect.tienda.domain.Categories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriesDao extends JpaRepository<Categories, Long> {
}
