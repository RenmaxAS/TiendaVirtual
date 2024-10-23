package com.proyect.tienda.web;

import com.proyect.tienda.domain.Categories;
import com.proyect.tienda.domain.Cliente;
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
    public String guardar(@ModelAttribute Categories categories,Errors errores, RedirectAttributes redirectAttributes, Model model){
        String mensajeExito;
        if (errores.hasErrors()) {
            model.addAttribute("mensajeError", "Errores en el formulario. Por favor, verifica los campos.");
            return "components/categoria/categoriesForm";
        }

        // Verificar si el cliente ya existe, excluyendo el ID del cliente actual (para el caso de edición)
        boolean categoriaExiste;
        if (categories.getIdCategoria() != null) {
            categoriaExiste = categoriesService.existeCategoriaExcluyendoId(categories.getIdCategoria(), categories.getCategoria());
        } else {
            categoriaExiste = categoriesService.existeCategoria(categories.getCategoria());
        }

        if (categoriaExiste) {
            String mensajeError = "La categoria " + categories.getCategoria() + " ya existe";
            model.addAttribute("mensajeError", mensajeError);
            model.addAttribute("categorias", categories); // Asegúrate de agregar esto
            return "components/categoria/categoriesForm";
        }

        if(categories.getIdCategoria() != null){
            // Si el producto tiene un ID, se está editando
            categoriesService.guardarCategoria(categories);
            mensajeExito = "Categoria editado exitosamente";
        } else {
            // Si el producto no tiene un ID, se está creando uno nuevo
            categoriesService.guardarCategoria(categories);
            mensajeExito = "Categoria guardado exitosamente";
        }

        redirectAttributes.addFlashAttribute("mensajeExito", mensajeExito);
        return "redirect:/categoria/list";
    }

    @GetMapping("/editarCategoria/{idCategoria}")
    public String editar(@PathVariable Long idCategoria, Model model) {
        Categories categorias = categoriesService.encontrarCategoriaPorId(idCategoria);
        model.addAttribute("categorias", categorias);
        return "components/categoria/categoriesForm";
    }

    @GetMapping("/eliminarCategoria")
    public String eliminar(Categories categories, RedirectAttributes redirectAttributes){
        try {
            categoriesService.eliminarCategoria(categories);
            redirectAttributes.addFlashAttribute("mensajeExito", "Categoria eliminado exitosamente");
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("mensajeError", "Error al eliminar la categoria");
        }
        return "redirect:/categoria/list";
    }

}
