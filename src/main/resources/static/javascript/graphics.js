document.addEventListener("DOMContentLoaded", function() {
    // Gráfico de ganancias del día real durante la semana
    fetch('/ganancias-semanaReal')
        .then(response => response.json())
        .then(data => {
            const labels = data.map(item => item.fecha);
            const totals = data.map(item => item.total);

            const ctx = document.getElementById('gananciasRealChart').getContext('2d');
            new Chart(ctx, {
                type: 'bar',
                data: {
                    labels: labels,
                    datasets: [{
                        label: 'Ganancias',
                        data: totals,
                        backgroundColor: 'rgba(255, 165, 0, 0.6)',
                        borderColor: 'rgba(255, 165, 0, 1)',
                        borderWidth: 1
                    }]
                },
                options: {
                    scales: {
                        y: {
                            beginAtZero: true,
                            ticks: {
                                stepSize: 10
                            }
                        }
                    }
                }
            });
        })
        .catch(error => console.error('Error al cargar datos para ganancias reales:', error));

    // Gráfico de ganancias del día total durante la semana
    fetch('/ganancias-semanaTotal')
        .then(response => response.json())
        .then(data => {
            const labels = data.map(item => item.fecha);
            const totals = data.map(item => item.total);

            const ctx = document.getElementById('gananciasTotalChart').getContext('2d');
            new Chart(ctx, {
                type: 'bar',
                data: {
                    labels: labels,
                    datasets: [{
                        label: 'Ganancias',
                        data: totals,
                        backgroundColor: 'rgba(255, 165, 0, 0.6)',
                        borderColor: 'rgba(255, 165, 0, 1)',
                        borderWidth: 1
                    }]
                },
                options: {
                    scales: {
                        y: {
                            beginAtZero: true,
                            ticks: {
                                stepSize: 10
                            }
                        }
                    }
                }
            });
        })
        .catch(error => console.error('Error al cargar datos para ganancias totales:', error));
});