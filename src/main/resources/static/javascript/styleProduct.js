$(document).ready(function() {
    var selectedId;

    // Inicialización de DataTables
    $('#productosTable').DataTable({
        "paging": true,              // Habilita la paginación
        "lengthChange": false,       // Deshabilita la opción de cambiar el número de registros mostrados por página
        "searching": true,           // Habilita la caja de búsqueda
        "ordering": true,            // Habilita el ordenamiento de columnas
        "info": true,                // Muestra la información de los registros
        "autoWidth": true,          // Deshabilita el ajuste automático del ancho de las columnas
        "pageLength": 4,             // Establece el límite de filas por página
        "language": {
            "paginate": {
                "previous": "Previous",
                "next": "Next"
            }
        }
    });

    // Funcionalidad de selección de filas
    $('#productosTable tbody').on('click', 'tr', function() {
        if ($(this).hasClass('selected')) {
            $(this).removeClass('selected');
            selectedId = null;
            $('#editButton').prop('disabled', true);
            $('#deleteButton').prop('disabled', true);
            $('#reactiveButton').prop('disabled', true);
        } else {
            $('#productosTable tbody tr').removeClass('selected');
            $(this).addClass('selected');
            selectedId = $(this).find('td:first').text();
            $('#editButton').prop('disabled', false);
            $('#deleteButton').prop('disabled', false);
            $('#reactiveButton').prop('disabled', false);
        }
    });

    // Funcionalidad de los botones Editar y Eliminar
    $('#editButton').on('click', function() {
        if (selectedId) {
            window.location.href = '/editarProducto/' + selectedId;
        }
    });

    $('#deleteButton').on('click', function() {
        if (selectedId) {
            if (confirm('¿Estás seguro que deseas eliminar este producto?')) {
                window.location.href = '/eliminarProducto?idProducto=' + selectedId;
            }
        }
    });

    $('#reactiveButton').on('click', function() {
        if (selectedId) {
            if (confirm('¿Estás seguro que deseas reactivar este producto?')) {
                window.location.href = '/reactivarProducto?idProducto=' + selectedId;
            }
        }
    });
});

/*Para asignar los valores de stock con numeros enteros y decimales*/
document.addEventListener("DOMContentLoaded", function() {
    // Recorrer todas las celdas de stock y formatearlas
    document.querySelectorAll('.stock').forEach(function(cell) {
        const stockValue = parseFloat(cell.textContent);
        cell.textContent = stockValue % 1 === 0 ? stockValue.toFixed(0) : stockValue.toFixed(3);
    });
});

/*Para filtrar aquellos productos que están por vencerse*/
/*Para filtrar aquellos productos que están vencidos*/
function filtrarProductos(estado) {
    if (estado === 'VENCER') {
        window.location.href = '/producto/list?vencer=true';
    } else if (estado === 'VENCIDO') {
        window.location.href = '/producto/list?vencido=true';
    } else {
        window.location.href = '/producto/list?estado=' + estado;
    }
}