$(document).ready(function() {
    var selectedId;

    // Inicialización de DataTables
    $('#clientesTable').DataTable({
        "paging": true,              // Habilita la paginación
        "lengthChange": false,       // Deshabilita la opción de cambiar el número de registros mostrados por página
        "searching": true,          // Deshabilita la caja de búsqueda
        "ordering": true,            // Habilita el ordenamiento de columnas
        "info": true,                // Muestra la información de los registros (por ejemplo, "Mostrando 1 a 10 de 50 entradas")
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
    $('#clientesTable tbody').on('click', 'tr', function() {
        if ($(this).hasClass('selected')) {
            $(this).removeClass('selected');
            selectedId = null;
            $('#editButton').prop('disabled', true);
            $('#deleteButton').prop('disabled', true);
            $('#reactiveButton').prop('disabled', true);
        } else {
            $('#clientesTable tbody tr').removeClass('selected');
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
            window.location.href = '/editarCliente/' + selectedId;
        }
    });

    $('#deleteButton').on('click', function() {
        if (selectedId) {
            if (confirm('¿Estás seguro que deseas eliminar este cliente?')) {
                window.location.href = '/eliminarCliente?idCliente=' + selectedId;
            }
        }
    });

    $('#reactiveButton').on('click', function() {
        if (selectedId) {
            if (confirm('¿Estás seguro que deseas reactivar este cliente?')) {
                window.location.href = '/reactivarCliente?idCliente=' + selectedId;
            }
        }
    });
});

function filtrarClientes(estado) {
    window.location.href = '/cliente/list?estado=' + estado;
}