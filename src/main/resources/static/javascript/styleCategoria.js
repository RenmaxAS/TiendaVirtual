$(document).ready(function() {
    var selectedId;
    var menu = $('#contextMenu');

    // Inicialización de DataTables
    $('#categoriasTable').DataTable({
        "paging": true,
        "lengthChange": false,
        "searching": true,
        "ordering": true,
        "info": true,
        "autoWidth": true,
        "pageLength": 10,
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

    $('#categoriasTable tbody').on('click', 'tr', function() {
        if ($(this).hasClass('selected')) {
            $(this).removeClass('selected');
            $(this).css('background-color', ''); // Restablece el color original de la fila
            selectedId = null;
            $('#editButton').prop('disabled', true);
            $('#deleteButton').prop('disabled', true);
        } else {
            $('#categoriasTable tbody tr').removeClass('selected').css('background-color', ''); // Quita la selección de todas las filas y restablece el color
            $(this).addClass('selected');
            $(this).css('background-color', '#FFA500'); // Color de resaltado para la fila seleccionada
            selectedId = $(this).find('td:first').text();
            $('#editButton').prop('disabled', false);
            $('#deleteButton').prop('disabled', false);
        }
    });

    $('#categoriasTable tbody').on('contextmenu', 'tr', function(e) {
        e.preventDefault();
        $('#categoriasTable tbody tr').removeClass('selected');
        $(this).addClass('selected');
        selectedId = $(this).find('td:first').text();

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

    // Funcionalidades de los botones del menú contextual
    $('#editOption').on('click', function() {
        if (selectedId) {
            window.location.href = '/editarCategoria/' + selectedId;
        }
    });

    $('#deleteOption').on('click', function() {
        if (selectedId) {
            if (confirm('¿Estás seguro que deseas eliminar este cliente?')) {
                window.location.href = '/eliminarCategoria?idCategoria=' + selectedId;
            }
        }
    });
});