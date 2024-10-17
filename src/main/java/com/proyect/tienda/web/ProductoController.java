package com.proyect.tienda.web;

import com.proyect.tienda.domain.Producto;
import com.proyect.tienda.service.ProductoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Controller
@Slf4j
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @GetMapping("/producto/list")
    public String productoList(@RequestParam(value = "estado", required = false) String estado,
                               @RequestParam(value = "vencer", required = false) Boolean vencer,
                               @RequestParam(value = "vencido", required = false) Boolean vencido,
                               Model model) {
        List<Producto> productos;
        if (Boolean.TRUE.equals(vencer)) {
            productos = productoService.listarProductosPorVencer();
        } else if (Boolean.TRUE.equals(vencido)){
            productos = productoService.listarProductosVencidos();
        } else if (estado == null || "A".equalsIgnoreCase(estado)) {
            productos = productoService.listarProductosPorEstado("A");
        } else if ("I".equalsIgnoreCase(estado)) {
            productos = productoService.listarProductosPorEstado("I");
        } else {
            productos = productoService.listarProducto();
        }
        model.addAttribute("productos", productos);
        return "components/producto/productoList";
    }

    @GetMapping("/agregar/producto")
    public String agregar(Producto producto){
        return "components/producto/productoForm";
    }

    @PostMapping("/guardar/producto")
    public String guardarProducto(@ModelAttribute Producto producto,
                                  @RequestParam("file") MultipartFile file,
                                  Errors errores, Model model, RedirectAttributes redirectAttributes) throws IOException {
        String mensajeExito;
        if (errores.hasErrors()) {
            model.addAttribute("mensajeError", "Errores en el formulario. Por favor, verifica los campos.");
            return "components/producto/productoForm";
        }

        // Verificar si el producto ya existe, excluyendo el ID del producto actual (para el caso de edición)
        boolean productoExiste;
        if (producto.getIdProducto() != null) {
            productoExiste = productoService.existeProductoExcluyendoId(producto.getIdProducto(), producto.getNombre(), producto.getMarca(), producto.getFormato());
        } else {
            productoExiste = productoService.existeProducto(producto.getNombre(), producto.getMarca(), producto.getFormato());
        }

        if (productoExiste) {
            model.addAttribute("mensajeError", "El producto ya existe.");
            return "components/producto/productoForm";
        }

        String uploadDir = "src/main/resources/static/images/";

        // Cargar el producto existente si tiene un ID
        if (producto.getIdProducto() != null) {
            Producto productoExistente = productoService.encontrarProductoPorId(producto.getIdProducto());
            if (productoExistente != null) {
                // Si no se proporciona una nueva imagen, mantén la ruta de la imagen existente
                if (file.isEmpty()) {
                    producto.setImagen(productoExistente.getImagen());
                } else {
                    // Eliminar la imagen antigua si existe
                    if (productoExistente.getImagen() != null && !productoExistente.getImagen().isEmpty()) {
                        Path oldPath = Paths.get("src/main/resources/static/images/" + Paths.get(productoExistente.getImagen()).getFileName().toString());
                        if (Files.exists(oldPath)) {
                            Files.delete(oldPath);
                        }
                    }
                }
            }
        }

        if (!file.isEmpty()) {
            try {
                // Crear el directorio de destino si no existe
                Path uploadPath = Paths.get(uploadDir);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                // Eliminar la imagen antigua si existe
                if (producto.getIdProducto() != null) {
                    Producto productoExistente = productoService.encontrarProductoPorId(producto.getIdProducto());
                    if (productoExistente != null && productoExistente.getImagen() != null && !productoExistente.getImagen().isEmpty()) {
                        Path oldPath = Paths.get("src/main/resources/static/images/" + Paths.get(productoExistente.getImagen()).getFileName().toString());
                        if (Files.exists(oldPath)) {
                            Files.delete(oldPath);
                        }
                    }
                }

                // Guardar la nueva imagen
                String fileName = StringUtils.cleanPath(file.getOriginalFilename());
                Path path = Paths.get(uploadDir + fileName);
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

                // Guardar la nueva ruta de la imagen en el producto
                producto.setImagen("/static/images/" + fileName);
            } catch (IOException e) {
                e.printStackTrace();
                model.addAttribute("mensajeError", "Error al subir la imagen. Intenta de nuevo.");
                return "components/producto/productoForm";
            }
        }

        if (producto.getEstado() == null || producto.getEstado().isEmpty()) {
            producto.setEstado("A");
        } else {
            producto.setEstado(producto.getEstado());
        }

        if (producto.getIdProducto() != null) {
            // Si el producto tiene un ID, se está editando
            productoService.guardar(producto);
            mensajeExito = "Producto editado exitosamente.";
        } else {
            // Si el producto no tiene un ID, se está creando uno nuevo
            productoService.guardar(producto);
            mensajeExito = "Producto registrado exitosamente.";
        }

        redirectAttributes.addFlashAttribute("mensajeExito", mensajeExito);
        return "redirect:/producto/list";
    }

    @GetMapping("/editarProducto/{idProducto}")
    public String editar(@PathVariable Long idProducto, Model model) {
        Producto producto = productoService.encontrarProductoPorId(idProducto);

        if (producto == null) {
            // Manejar el caso donde el producto no existe
            return "error"; // Puedes redirigir a una página de error personalizada
        }

        // Agregar el producto al modelo
        model.addAttribute("producto", producto);

        return "components/producto/productoForm";
    }

    @GetMapping("/eliminarProducto")
    public String eliminar(Producto producto, RedirectAttributes redirectAttributes){
        productoService.eliminar(producto);
        redirectAttributes.addFlashAttribute("mensajeExito", "Producto eliminado exitosamente");
        return "redirect:/producto/list";
    }

    @GetMapping("/reactivarProducto")
    public String reactivar(Producto producto, RedirectAttributes redirectAttributes){
        productoService.reactivar(producto);
        redirectAttributes.addFlashAttribute("mensajeExito", "Producto reactivado exitosamente");
        return "redirect:/producto/list";
    }

}
