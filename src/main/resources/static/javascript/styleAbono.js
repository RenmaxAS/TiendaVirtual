$(document).ready(function () {
    $('#clienteBusqueda').on('input', function () {
        var searchTerm = $(this).val().toLowerCase();
        var count = 0;
        $('#clienteId option').each(function () {
            var clienteNombre = $(this).text().toLowerCase();
            if (clienteNombre.includes(searchTerm) && count < 5) {
                $(this).show();
                count++;
            } else {
                $(this).hide();
            }
        });
        $('#clienteId').val(''); // Reset the select value
    });

    $('#deudasTable').DataTable(); // Inicializar DataTable para la tabla de ventas
    $('#abonosTable').DataTable(); // Inicializar DataTable para la tabla de abonos
});

$(document).ready(function () {
    // Inicializar DataTable para la tabla de ventas
    $('#deudasTable').DataTable();

    // Inicializar DataTable para cada tabla de abonos cuando se muestre el modal
    $('body').on('shown.bs.modal', 'div.modal', function () {
        var modalId = $(this).attr('id');
        var ventaId = modalId.replace('abonosModal_', ''); // Obtener el ID de la venta
        $('#abonosTable_' + ventaId).DataTable();
    });
});