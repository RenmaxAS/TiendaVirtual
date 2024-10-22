package com.proyect.tienda.web;

import com.proyect.tienda.dao.AbonoDao;
import com.proyect.tienda.dao.VentaDao;
import com.proyect.tienda.domain.Cliente;
import com.proyect.tienda.domain.Producto;
import com.proyect.tienda.service.ClienteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @Autowired(required = false)
    private AbonoDao abonoDao;

    @Autowired(required = false)
    private VentaDao ventaDao;

    @GetMapping("/")
    public String inicio(Model model){
        BigDecimal gananciaDelDiaReal = abonoDao.sumAbonosDelDiaReal();
        if (gananciaDelDiaReal == null) {
            gananciaDelDiaReal = BigDecimal.ZERO;
        }
        BigDecimal gananciaDelDiaTotal = ventaDao.sumAbonosDelDiaTotal();
        if (gananciaDelDiaTotal == null){
            gananciaDelDiaTotal = BigDecimal.ZERO;
        }
        model.addAttribute("gananciaDelDiaReal", gananciaDelDiaReal);
        model.addAttribute("gananciaDelDiaTotal", gananciaDelDiaTotal);
        return "index";
    }

    @GetMapping("/ganancias-semanaReal")
    @ResponseBody
    public List<Map<String, Object>> getGananciasDeLaSemanaReal() {
        return abonoDao.findGananciasDeLaSemanaReal();
    }

    @GetMapping("/ganancias-semanaTotal")
    @ResponseBody
    public List<Map<String, Object>> getGananciasDeLaSemanaTotal() {
        return ventaDao.findGananciasDeLaSemanaTotal();
    }

    @GetMapping("/cliente/list")
    public String clienteList(@RequestParam(value = "estado", required = false) String estado,
                              Model model) {
        List<Cliente> clientes;
        if (estado == null) {
            return "redirect:/cliente/list?estado=A";
        }
        if(estado == null || "A".equalsIgnoreCase(estado)){
            clientes = clienteService.listarClientesPorEstado(estado);
        } else if ("I".equalsIgnoreCase(estado)){
            clientes = clienteService.listarClientesPorEstado(estado);
        } else {
            clientes = clienteService.listarCliente();
        }
        model.addAttribute("clientes", clientes);
        return "components/cliente/clienteList";
    }

    @GetMapping("/agregar/cliente")
    public String agregar(Model model){
        model.addAttribute("cliente", new Cliente());
        return "components/cliente/clienteForm";
    }

    @PostMapping("/guardar/cliente")
    public String guardar(Cliente cliente, Errors errores, RedirectAttributes redirectAttributes) {

        String mensajeExito;
        // Validar si el cliente ya existe antes de guardar
        if (clienteService.findByNombreAndApellido(cliente.getNombre(), cliente.getApellido())) {
            String mensajeError = "El cliente: " + cliente.getNombre() + " " + cliente.getApellido() + " ya existe";
            redirectAttributes.addFlashAttribute("mensajeError", mensajeError);
            return "redirect:/agregar/cliente"; // Redirigir al formulario con los errores
        }

        if (errores.hasErrors()) {
            return "components/cliente/clienteForm";
        }

        if (cliente.getIdCliente() != null && (cliente.getEstado() == null || cliente.getEstado().isEmpty())) {
            cliente.setEstado(cliente.getEstado());
            // Si el cliente tiene un ID, se está editando
            clienteService.guardar(cliente);
            mensajeExito = "Cliente editado exitosamente.";
        } else {
            cliente.setEstado("A");
            // Si el cliente no tiene un ID, se está creando uno nuevo
            clienteService.guardar(cliente);
            mensajeExito = "Cliente registrado exitosamente.";
        }

        redirectAttributes.addFlashAttribute("mensajeExito", mensajeExito);
        return "redirect:/cliente/list";
    }

    @GetMapping("/editarCliente/{idCliente}")
    public String editar(@PathVariable Long idCliente, Model model){
        Cliente cliente = clienteService.encontrarClientePorId(idCliente);
        model.addAttribute("cliente", cliente);
        return "components/cliente/clienteForm";
    }

    @GetMapping("/eliminarCliente")
    public String eliminar(Cliente cliente, RedirectAttributes redirectAttributes){
        try {
            clienteService.eliminar(cliente);
            redirectAttributes.addFlashAttribute("mensajeExito", "Cliente eliminado exitosamente");
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("mensajeError", "Error al eliminar el cliente");
        }
        return "redirect:/cliente/list";
    }

    @GetMapping("/reactivarCliente")
    public String reactivar(Cliente cliente, RedirectAttributes redirectAttributes){
        clienteService.reactivar(cliente);
        System.out.println("Cliente reacctivadoooo::::::::::::::");
        redirectAttributes.addFlashAttribute("mensajeExito", "Cliente reactivado exitosamente");
        return "redirect:/cliente/list";
    }


}
