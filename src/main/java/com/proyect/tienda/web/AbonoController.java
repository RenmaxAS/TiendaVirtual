package com.proyect.tienda.web;

import com.proyect.tienda.dao.AbonoDao;
import com.proyect.tienda.dao.ClienteDao;
import com.proyect.tienda.dao.VentaDao;
import com.proyect.tienda.domain.Abono;
import com.proyect.tienda.domain.Cliente;
import com.proyect.tienda.domain.Producto;
import com.proyect.tienda.domain.Venta;
import com.proyect.tienda.service.AbonoService;
import com.proyect.tienda.service.VentaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@Slf4j
@EnableGlobalMethodSecurity(prePostEnabled=true,securedEnabled = true, jsr250Enabled = true)
public class AbonoController {

    @Autowired
    private AbonoService abonoService;

    @Autowired(required = false)
    private AbonoDao abonoDao;

    @Autowired(required = false)
    private ClienteDao clienteDao;

    @Autowired
    private VentaService ventaService;

    @Autowired(required = false)
    private VentaDao ventaDao;

    @GetMapping("/abonar/nuevo")
    public String agregar(Model model){
        List<Cliente> clientes = clienteDao.findAll().stream()
                .filter(cliente -> "A".equals(cliente.getEstado()))
                .collect(Collectors.toList());
        model.addAttribute("clientes", clientes);
        return "components/abono/abonoForm";
    }

    @GetMapping("/abonar/deudas")
    public String obtenerDeudasPorCliente(@RequestParam("clienteId") int clienteId, Model model) {
        List<Venta> ventas = ventaDao.findByCliente_IdClienteAndEstado(clienteId, "D");
        model.addAttribute("ventas", ventas);

        Cliente clienteSeleccionado = clienteDao.findById((long) clienteId).orElse(null);
        model.addAttribute("clienteSeleccionado", clienteSeleccionado);

        List<Cliente> clientes = clienteDao.findAll().stream()
                .filter(cliente -> "A".equals(cliente.getEstado()))
                .collect(Collectors.toList());
        model.addAttribute("clientes", clientes);

        return "components/abono/abonoForm";
    }

    @PostMapping("/abonar/nuevoAbono")
    public String registrarNuevoAbono(@RequestParam("ventaId") Long ventaId,
                                      @RequestParam("abono") BigDecimal abono,
                                      RedirectAttributes redirectAttributes) {
        Venta venta = ventaDao.findById(ventaId)
                .orElseThrow(() -> new IllegalArgumentException("Venta no encontrada"));

        // Obtener el último abono registrado para calcular la deuda actual
        BigDecimal deudaActual;
        try {
            deudaActual = abonoDao.findUltimaDeudaByVentaId(ventaId);
            // Registrar el valor de deudaActual para depuración
            System.out.println("Deuda Actual: " + deudaActual);
        } catch (Exception e) {
            // Manejar posibles errores y registrar el error para depuración
            System.err.println("Error obteniendo la deuda actual: " + e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Error obteniendo la deuda actual.");
            return "redirect:/abonar/deudas?clienteId=" + venta.getCliente().getIdCliente();
        }

        // Manejar el caso en el que la deuda actual es nula
        if (deudaActual == null) {
            redirectAttributes.addFlashAttribute("error", "No se encontró la deuda actual.");
            return "redirect:/abonar/deudas?clienteId=" + venta.getCliente().getIdCliente();
        }

        if(!(abono.compareTo(deudaActual) > 0)){
            BigDecimal nuevaDeuda = deudaActual.subtract(abono);

            // Verificar si la nueva deuda es menor o igual a cero
            if (nuevaDeuda.compareTo(BigDecimal.ZERO) <= 0) {
                // Actualizar el estado de la venta a "P" (Pagado)
                venta.setEstado("P");
            }

            // Crear y guardar el nuevo abono
            Abono nuevoAbono = new Abono();
            nuevoAbono.setVenta(venta);
            nuevoAbono.setAbono(abono);
            nuevoAbono.setDeuda(nuevaDeuda);
            nuevoAbono.setFechaAbono(new Date());

            abonoDao.save(nuevoAbono);

            // Guardar la venta actualizada
            ventaDao.save(venta);

            redirectAttributes.addFlashAttribute("mensajeExito", "Abono registrado exitosamente.");
        }else {
            redirectAttributes.addFlashAttribute("mensajeError", "EL abono no puede ser mayor a la deuda actual.");
        }
        return "redirect:/abonar/deudas?clienteId=" + venta.getCliente().getIdCliente();
    }
}
