/*Para el gráfico de ganancias del día real durante la semana*/
document.addEventListener("DOMContentLoaded", function() {
    fetch('/ganancias-semanaReal')
        .then(response => response.json())
        .then(data => {
            const labels = data.map(item => item.fecha);
            const totals = data.map(item => item.total);

            const ctx = document.getElementById('gananciasRealChart').getContext('2d');
            const gananciasRealChart = new Chart(ctx, {
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
        });
});

/*Para el gráfico de ganancias del día total durante la semana*/
document.addEventListener("DOMContentLoaded", function() {
    fetch('/ganancias-semanaTotal')
        .then(response => response.json())
        .then(data => {
            const labels = data.map(item => item.fecha);
            const totals = data.map(item => item.total);

            const ctx = document.getElementById('gananciasTotalChart').getContext('2d');
            const gananciasTotalChart = new Chart(ctx, {
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
        });
});