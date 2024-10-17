package com.proyect.tienda.web;

import com.proyect.tienda.domain.Categories;
import com.proyect.tienda.service.CategoriesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@Slf4j
public class CategoriesController {

    @Autowired
    private CategoriesService categoriesService;

    @GetMapping("/categoria/list")
    public String categoriaList(Model model){
        List<Categories> categorias;
        categorias = categoriesService.listarCategoria();
        model.addAttribute("categorias", categorias);
        return "components/categoria/categoriesList";
    }

    @GetMapping("/agregar/categoria")
    public String agregar(Model model){
        model.addAttribute("categorias", new Categories());
        return "components/categoria/categoriesForm";
    }

    @PostMapping("/guardar/categoria")
    public String guardar(Categories categories, Errors errores, RedirectAttributes redirectAttributes){
        String mensajeExito;
        categoriesService.guardarCategoria(categories);
        mensajeExito = "Categoria guardado exitosamente";
        redirectAttributes.addFlashAttribute("mensajeExito", mensajeExito);
        return "redirect:/categoria/list";
    }
}
