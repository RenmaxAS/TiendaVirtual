package com.proyect.tienda.dao;

import com.proyect.tienda.domain.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductoDao extends JpaRepository<Producto, Long> {
    List<Producto> findByEstado(String estado);

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Producto p WHERE p.nombre = :nombre AND p.marca = :marca AND p.formato = :formato")
    boolean existsByNombreAndMarcaAndFormato(@Param("nombre") String nombre, @Param("marca") String marca, @Param("formato") String formato);

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Producto p WHERE p.nombre = :nombre AND p.marca = :marca AND p.formato = :formato AND p.idProducto <> :id")
    boolean existsByNombreAndMarcaAndFormatoAndNotId(String nombre, String marca, String formato, Long id);

    List<Producto> findByNombreContainingIgnoreCase(String term);

    // Nueva consulta para productos que están a un mes de caducar
    @Query(value = "SELECT * FROM producto WHERE DATE(Fecha_Vencimiento) BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL 1 MONTH) AND Estado = 'A'", nativeQuery = true)
    List<Producto> findProductosPorVencer();

    @Query(value = "SELECT * FROM producto WHERE CURDATE() > DATE(Fecha_Vencimiento) AND Estado = 'A'", nativeQuery = true)
    List<Producto> findProductosVencidos();
}
