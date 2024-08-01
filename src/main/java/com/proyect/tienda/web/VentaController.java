package com.proyect.tienda.web;

import com.proyect.tienda.dao.AbonoDao;
import com.proyect.tienda.dao.ClienteDao;
import com.proyect.tienda.dao.ProductoDao;
import com.proyect.tienda.domain.*;
import com.proyect.tienda.service.VentaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@Slf4j
@SessionAttributes("venta")
public class VentaController {

    @Autowired(required = false)
    private ProductoDao productoDao;

    @Autowired(required = false)
    private ClienteDao clienteDao;

    @Autowired
    private VentaService ventaService;

    @Autowired(required = false)
    private AbonoDao abonoDao;

    @GetMapping("/ventas/nueva")
    public String nuevaVenta(Model model) {
        List<Cliente> clientes = clienteDao.findAll().stream()
                .filter(cliente -> "A".equals(cliente.getEstado()))
                .collect(Collectors.toList());
        List<Producto> productos = productoDao.findAll().stream()
                .filter(producto -> "A".equals(producto.getEstado()))
                .collect(Collectors.toList());
        model.addAttribute("clientes", clientes);
        model.addAttribute("productos", productos);
        model.addAttribute("venta", new Venta());
        return "components/venta/ventaForm";
    }

    @PostMapping("/ventas/registrar")
    public String registrarVenta(@RequestParam("clienteId") Long clienteId,
                                 @RequestParam(value = "abono", required = false, defaultValue = "0.00") BigDecimal abonoMonto,
                                 @ModelAttribute("venta") Venta venta,
                                 Model model, RedirectAttributes redirectAttributes, SessionStatus sessionStatus) {
        String sobreStock;
        log.info("Registrando venta para clienteId: {}", clienteId);

        Cliente cliente = clienteDao.findById(clienteId).orElse(null);
        if (cliente == null) {
            redirectAttributes.addFlashAttribute("mensajeError", "Cliente no encontrado.");
            return "components/venta/ventaForm";
        }

        venta.setCliente(cliente);
        venta.setFechaPedido(new Date()); // Establecer la fecha actual

        if (venta.getVentaDetalles() == null || venta.getVentaDetalles().isEmpty()) {
            redirectAttributes.addFlashAttribute("mensajeError", "Debe agregar al menos un producto.");
            return "components/venta/ventaForm";
        }

        BigDecimal total = BigDecimal.ZERO;
        for (VentaDetalle ventaDetalle : venta.getVentaDetalles()) {
            Producto producto = productoDao.findById(ventaDetalle.getProducto().getIdProducto()).orElse(null);
            if (producto == null) {
                redirectAttributes.addFlashAttribute("mensajeError", "Producto no encontrado.");
                return "components/venta/ventaForm";
            }
            ventaDetalle.setProducto(producto);
            ventaDetalle.setVenta(venta);

            // Calcular y asignar el subtotal
            BigDecimal subTotal = producto.getPrecio().multiply(new BigDecimal(String.valueOf(ventaDetalle.getCantidad())));
            ventaDetalle.setSubTotal(subTotal);

            total = total.add(subTotal);
        }

        venta.setImporteTotal(total);

        // Guardar la venta
        try {
            if(!(abonoMonto.compareTo(venta.getImporteTotal())>0)){
                ventaService.crearVenta(venta);

                // Registrar el abono
                Abono abono = new Abono();
                abono.setVenta(venta);
                abono.setAbono(abonoMonto);
                abono.setFechaAbono(new Date());

                BigDecimal deuda = total.subtract(abonoMonto);
                abono.setDeuda(deuda);

                if (deuda.compareTo(BigDecimal.ZERO) == 0) {
                    venta.setEstado("P"); // Pagado
                } else {
                    venta.setEstado("D"); // Deuda
                }

                abonoDao.save(abono);

                redirectAttributes.addFlashAttribute("mensajeExito", "Venta registrada exitosamente");
                log.info("Venta registrada correctamente.");
                sessionStatus.setComplete(); // Limpiar el atributo de sesión
                return "redirect:/ventas/nueva";
            } else {
                redirectAttributes.addFlashAttribute("mensajeError", "EL abono no puede ser mayor que el importe total.");
            }
            return "redirect:/ventas/nueva";

        } catch (Exception e) {
            log.error("Error al registrar la venta: " + e.getMessage());
            sobreStock = "Error al registrar la venta" + e.getMessage();
            model.addAttribute("mensajeError", sobreStock);
            return "components/venta/ventaForm";
        }
    }

    @GetMapping("/ventas/list")
    public String listarVentas(@RequestParam(required = false) Long clienteId,
                               @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date fechaDesde,
                               @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date fechaHasta,
                               Model model) {
        List<Cliente> clientes = clienteDao.findAll();
        model.addAttribute("clientes", clientes);

        // Inicializar el cliente seleccionado como nulo
        Cliente clienteSeleccionado = null;
        if (clienteId != null) {
            clienteSeleccionado = clienteDao.findById(clienteId).orElse(null);
        }
        model.addAttribute("clienteSeleccionado", clienteSeleccionado);

        List<Venta> ventas = List.of();
        BigDecimal totalImporte = BigDecimal.ZERO;

        if (clienteId != null && fechaDesde != null && fechaHasta != null) {
            ventas = ventaService.findVentasByClienteAndFecha(clienteId, fechaDesde, fechaHasta);
            totalImporte = ventas.stream()
                    .map(Venta::getImporteTotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }

        model.addAttribute("ventas", ventas);
        model.addAttribute("totalImporte", totalImporte);
        return "components/venta/ventaList";
    }
}
