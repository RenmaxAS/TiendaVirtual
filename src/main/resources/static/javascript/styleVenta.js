$(document).ready(function() {
    $('#productosTable').DataTable({
        paging: false,
        searching: false,
        info: false
    });
});

/*Para aplicar un filtro en la busqueda de cliente y productos*/
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
});

$(document).ready(function () {
    $('#productoBusqueda').on('input', function () {
        var searchTerm = $(this).val().toLowerCase();
        var count = 0;
        $('#productoId option').each(function () {
            var productoNombre = $(this).text().toLowerCase();
            if (productoNombre.includes(searchTerm) && count < 5) {
                $(this).show();
                count++;
            } else {
                $(this).hide();
            }
        });
        $('#productoId').val(''); // Reset the select value
    });
});

/*Para agregar productos a la tabla temporal*/
function productoYaAgregado(idProducto) {
    var productosTable = $('#productosTable tbody');
    var filas = productosTable.find('tr');
    var encontrado = false;

    filas.each(function(index, fila) {
        var productoId = $(fila).find('input[name^="ventaDetalles["]').val();
        if (productoId == idProducto) {
            encontrado = true;
            return false; // Terminar el bucle each() si se encontró el producto
        }
    });

    return encontrado;
}

function agregarProducto() {
    var productoId = $('#productoId').val();
    var productoText = $('#productoId option:selected').text();
    var productoStock = parseFloat($('#productoId option:selected').data('stock'));
    var cantidad = parseFloat($('#cantidad').val());
    var precio = parseFloat($('#productoId option:selected').data('price'));

    if (productoId && cantidad > 0 && !isNaN(precio)) {
        if (productoYaAgregado(productoId)) {
            alert('Este producto ya ha sido agregado.');
            return;
        }

        var subtotal = (cantidad * precio).toFixed(2);

        $('#productosTable tbody').append(`
            <tr>
                <td>
                    <input type="hidden" name="ventaDetalles[${$('#productosTable tbody tr').length}].producto.idProducto" value="${productoId}">
                    ${productoText}
                </td>
                <td>
                    <input type="hidden" name="ventaDetalles[${$('#productosTable tbody tr').length}].producto.stock" value="${productoStock}">
                    ${productoStock}
                </td>
                <td>
                    <input type="number" class="form-control cantidad-input" name="ventaDetalles[${$('#productosTable tbody tr').length}].cantidad" value="${cantidad}" step="0.001" min="0">
                </td>
                <td class="precio-unitario">${precio}</td>
                <td class="subtotal">
                    ${subtotal}
                    <input type="hidden" class="subtotal-input" name="ventaDetalles[${$('#productosTable tbody tr').length}].subTotal" value="${subtotal}">
                </td>
                <td>
                    <button type="button" class="btn btn-danger" onclick="eliminarProducto(this)">
                        <i class="bi bi-cart-x-fill"></i>
                    </button>
                </td>
            </tr>
        `);

        actualizarTotales();

        // Reiniciar los campos de selección y cantidad
        $('#productoId').val('');
        $('#cantidad').val(1);
    } else {
        alert('Seleccione un producto, cantidad válida y asegúrese de que el producto tenga un precio válido.');
    }
}

function eliminarProducto(btn) {
    $(btn).closest('tr').remove();
    actualizarTotales();
}

// Escuchar cambios en la cantidad y actualizar subtotal
$(document).on('change', '.cantidad-input', function() {
    var fila = $(this).closest('tr');
    var cantidad = parseFloat($(this).val());
    var precio = parseFloat(fila.find('.precio-unitario').text()); // Obtener el precio de la columna con clase "precio-unitario"
    var subtotal = (cantidad * precio).toFixed(2);
    fila.find('.subtotal').html(subtotal + '<input type="hidden" class="subtotal-input" name="ventaDetalles[' + fila.index() + '].subTotal" value="' + subtotal + '">');
    actualizarTotales();
});

function actualizarTotales() {
    var total = 0;
    $('.subtotal-input').each(function() {
        total += parseFloat($(this).val());
    });
    $('#importeTotal').text(total.toFixed(2));
    $('#totalSubtotal').text(total.toFixed(2));
}
