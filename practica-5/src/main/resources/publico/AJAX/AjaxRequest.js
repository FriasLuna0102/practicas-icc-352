// function cargarArticulos(pagina) {
//     $.ajax({
//         url: '/blogUsuario/articuloAjax',
//         data: { pagina: pagina },
//         method: 'GET',
//         success: function(response) {
//             console.log(response);
//         }
//     });
// }

// Función para cargar artículos mediante AJAX
// Define la función para cargar los artículos mediante AJAX
$(document).ready(function() {
    // Función para cargar los artículos
    function cargarArticulos(pagina) {
        $.get("/blogUsuario/blogUsuarioAjax", { pagina: pagina }, function(data) {
            if (data !== null) {
                console.log('Respuesta del servidor:', data); // Agrega este registro para ver qué datos se reciben del servidor
                // Procesa la respuesta JSON y genera el HTML correspondiente
                var html = '';
                for (var i = 0; i < data.length; i++) {
                    var articulo = data[i];

                    html += '<h1 class="mayusculaallmoment"><a href="/blogUsuario/articulo/' + articulo.id + '">' + articulo.titulo + '</a></h1>';
                    html += '<p class="lead"><i class="fa fa-user"></i> <a href="">' + articulo.nombreAutor + '</a></p>';
                    html += '<hr>';
                    html += '<p> Posted on <i class="fa fa-calendar">' + articulo.fecha + '</i> </p>';
                    html += '<p><i class="fa fa-tags"></i> Tags: ';
                    // for (var j = 0; j < articulo.listaEtiquetas.length; j++) {
                    //     html += '<span class="badge badge-info">' + articulo.listaEtiquetas[j] + '</span> ';
                    // }
                    html += '</p>';
                    html += '<p>' + (articulo.cuerpo.length > 70 ? articulo.cuerpo.substring(0, 70) + '...' : articulo.cuerpo) + '</p>';
                }
                // Actualiza el contenido de la sección de blog con los nuevos artículos recibidos
                $('#ajaxx').html(html);
            } else {
                console.log('No se recibieron datos.');
            }
        });
    }

    // Maneja los clics en la paginación
    $(document).on('click', '#pagination .page-link', function(e) {
        e.preventDefault();
        var pagina = $(this).text(); // Obtén el número de página del texto del botón
        console.log('Página seleccionada:', pagina); // Agrega este registro para asegurarte de que la página seleccionada sea la correcta
        cargarArticulos(pagina); // Carga los artículos correspondientes a la página seleccionada
    });


    // Carga los artículos de la primera página al cargar la página inicialmente
    cargarArticulos(1);
});








