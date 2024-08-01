/* Para los mensajes de eventos */
document.addEventListener('DOMContentLoaded', function () {
    var toastEl = document.querySelector('.toast');
    var toast = new bootstrap.Toast(toastEl, {
        animation: true,
        autohide: true,
        delay: 5000
    });
    toast.show();
});

document.addEventListener('DOMContentLoaded', function () {
    var toastElList = [].slice.call(document.querySelectorAll('.toast'));
    var toastList = toastElList.map(function (toastEl) {
        return new bootstrap.Toast(toastEl, {
            autohide: false // Cambia a true si deseas que se cierren automáticamente después de un tiempo
        }).show();
    });
});