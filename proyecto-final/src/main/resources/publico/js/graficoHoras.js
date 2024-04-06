$.ajax({
    url: '/estadistica/info/horas',
    method: 'GET',
    dataType: 'json',
    success: function(data) {

        const ctx2 = document.getElementById('graficoHoras');

        new Chart(ctx2, {
            type: 'bar',
            data: {
                labels: ['00:00', '01:00', '02:00', '03:00', '04:00', '05:00', '06:00', '07:00', '08:00', '09:00', '10:00', '11:00', '12:00', '13:00', '14:00', '15:00', '16:00', '17:00', '18:00', '19:00', '20:00', '21:00', '22:00', '23:00'],

                datasets: [{
                    label: 'Cantidad total de accesos por hora',
                    data: Object.values(data),
                    borderWidth: 1
                }]
            },
            options: {
                scales: {
                    y: {
                        beginAtZero: false
                    }
                }
            }
        });
        console.log(Object.values(data))
    },
    error: function(jqXHR, textStatus, errorThrown) {
        console.error(errorThrown); // Manejar errores adecuadamente
    }
});
