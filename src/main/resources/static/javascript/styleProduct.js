$(document).ready(function() {
    var selectedId;
    var menu = $('#contextMenu');

    // Inicialización de DataTables
    $('#productosTable').DataTable({
        "paging": true,              // Habilita la paginación
        "lengthChange": false,       // Deshabilita la opción de cambiar el número de registros mostrados por página
        "searching": true,           // Habilita la caja de búsqueda
        "ordering": true,            // Habilita el ordenamiento de columnas
        "info": true,                // Muestra la información de los registros
        "autoWidth": true,          // Deshabilita el ajuste automático del ancho de las columnas
        "pageLength": 10,             // Establece el límite de filas por página
        "language": {
            "search": "Buscar:",
            "lengthMenu": "Mostrar _MENU_ registros por página",
            "zeroRecords": "No se encontraron resultados",
            "info": "Mostrando _START_ a _END_ de _TOTAL_ registros",
            "infoEmpty": "Mostrando 0 a 0 de 0 registros",
            "infoFiltered": "(filtrado de _MAX_ registros totales)",
            "paginate": {
                "previous": "Anterior",
                "next": "Siguiente"
            }
        }
    });

    // Funcionalidad de selección de filas
    $('#productosTable tbody').on('click', 'tr', function() {
        if ($(this).hasClass('selected')) {
            $(this).removeClass('selected');
            $(this).css('background-color', ''); // Restablece el color original de la fila
            selectedId = null;
            $('#editButton').prop('disabled', true);
            $('#deleteButton').prop('disabled', true);
            $('#reactiveButton').prop('disabled', true);
        } else {
            $('#productosTable tbody tr').removeClass('selected').css('background-color', ''); // Quita la selección de todas las filas y restablece el color
            $(this).addClass('selected');
            $(this).css('background-color', '#FFA500'); // Color de resaltado para la fila seleccionada
            selectedId = $(this).find('td:first').text();
            $('#editButton').prop('disabled', false);
            $('#deleteButton').prop('disabled', false);
            $('#reactiveButton').prop('disabled', false);
        }
    });

    $('#productosTable tbody').on('contextmenu', 'tr', function(e) {
        e.preventDefault();
        $('#productosTable tbody tr').removeClass('selected');
        $(this).addClass('selected');
        selectedId = $(this).find('td:first').text();

        // Obtén el estado de la fila seleccionada desde el atributo data
        var estado = $(this).data('estado');

        // Muestra u oculta la opción 'reactivar' según el estado
        if (estado === 'I') {
            $('#reactiveOption').show();
        } else {
            $('#reactiveOption').hide();
        }

        // Muestra u oculta la opción 'eliminar' según el estado
        if (estado === 'A') {
            $('#deleteOption').show();
        } else {
            $('#deleteOption').hide();
        }

        // Posicionar el menú contextual cerca del clic
        menu.css({
            display: "block",
            left: e.pageX,
            top: e.pageY
        });
    });

    // Ocultar el menú contextual si se hace clic fuera de él
    $(document).on('click', function(e) {
        if (!$(e.target).closest('#contextMenu').length) {
            menu.hide();
        }
    });

    // Funcionalidad de los botones Editar y Eliminar
    $('#editOption').on('click', function() {
        if (selectedId) {
            window.location.href = '/editarProducto/' + selectedId;
        }
    });

    $('#deleteOption').on('click', function() {
        if (selectedId) {
            if (confirm('¿Estás seguro que deseas eliminar este producto?')) {
                window.location.href = '/eliminarProducto?idProducto=' + selectedId;
            }
        }
    });

    $('#reactiveOption').on('click', function() {
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