package com.proyect.tienda.web;

import com.proyect.tienda.dao.ClienteDao;
import com.proyect.tienda.domain.Cliente;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.sql.DataSource;
import java.io.*;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
public class ReportController {

    @Autowired(required = false)
    private DataSource dataSource;

    @Autowired(required = false)
    private ClienteDao clienteDao;

    @GetMapping("/generateReport")
    public ResponseEntity<InputStreamResource> generateReport() {
        try {
            // Carga el archivo .jasper
            InputStream reportStream = getClass().getResourceAsStream("/reports/ClienteReports/To3Clientes.jasper");

            // Parámetros del reporte
            Map<String, Object> params = new HashMap<>();
            // Agrega tus parámetros aquí

            // Llena el reporte
            JasperPrint jasperPrint = JasperFillManager.fillReport(reportStream, params, dataSource.getConnection());

            // Exporta el reporte a PDF
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);

            // Prepara la respuesta
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("inline", "Top3Clientes.pdf");

            return new ResponseEntity<>(
                    new InputStreamResource(new ByteArrayInputStream(outputStream.toByteArray())),
                    headers,
                    HttpStatus.OK
            );
        } catch (JRException | SQLException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/generateReportSuport")
    public ResponseEntity<InputStreamResource> generateReportSuport(@RequestParam("clienteId") Integer clienteId) {
        try {
            // Carga el archivo .jasper
            InputStream reportStream = getClass().getResourceAsStream("/reports/VentaReports/HistorialDeudas.jasper");

            // Parámetros del reporte
            Map<String, Object> params = new HashMap<>();
            params.put("clienteId", clienteId);

            // Llena el reporte
            JasperPrint jasperPrint = JasperFillManager.fillReport(reportStream, params, dataSource.getConnection());

            // Exporta el reporte a PDF
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);

            // Obtener nombre y apellido del cliente
            Cliente cliente = clienteDao.findById(Long.valueOf(clienteId)).orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

            // Formatear la fecha actual del sistema
            String fechaActual = new SimpleDateFormat("dd-MM-yyyy").format(new Date());

            // Construir el nombre del archivo
            String nombreArchivo = cliente.getNombre() + cliente.getApellido() + "_HistorialDeuda_" + fechaActual + ".pdf";

            // Prepara la respuesta
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("inline", nombreArchivo);

            return new ResponseEntity<>(
                    new InputStreamResource(new ByteArrayInputStream(outputStream.toByteArray())),
                    headers,
                    HttpStatus.OK
            );
        } catch (JRException | SQLException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/generateReportProductList")
    public ResponseEntity<InputStreamResource> generateReportProductList() {
        try {
            // Carga el archivo .jasper
            InputStream reportStream = getClass().getResourceAsStream("/reports/ProductoReports/ListaProductosGanancias.jasper");

            // Parámetros del reporte
            Map<String, Object> params = new HashMap<>();
            // Agrega tus parámetros aquí

            // Llena el reporte
            JasperPrint jasperPrint = JasperFillManager.fillReport(reportStream, params, dataSource.getConnection());

            // Exporta el reporte a PDF
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);

            // Prepara la respuesta
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("inline", "Lista Productos y Ganancias.pdf");

            return new ResponseEntity<>(
                    new InputStreamResource(new ByteArrayInputStream(outputStream.toByteArray())),
                    headers,
                    HttpStatus.OK
            );
        } catch (JRException | SQLException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
