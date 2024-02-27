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
                console.log('Respuesta del servidor:', data);
                var html = '';
                for (var i = 0; i < data.length; i++) {
                    var articulo = data[i];

                    html += '<h1 class="mayusculaallmoment"><a href="/blogUsuario/articulo/' + articulo.id + '">' + articulo.titulo + '</a></h1>';
                    html += '<p class="lead"><i class="fa fa-user"></i> <a href="">' + articulo.nombreAutor + '</a></p>';
                    html += '<hr>';
                    var fecha = new Date(articulo.fecha);
                    var opciones = { year: 'numeric', month: 'long', day: 'numeric', hour : 'numeric', minute: 'numeric', second: 'numeric' };
                    html += '<p> Posted on <i class="fa fa-calendar">' + fecha.toLocaleDateString('es-ES', opciones) + '</i> </p>';
                    html += '<p><i class="fa fa-tags"></i> Tags: ';
                    for (var j = 0; j < articulo.listaEtiquetas.length; j++) {
                        html += '<span class="badge badge-info">' + articulo.listaEtiquetas[j] + '</span> ';
                    }
                    html += '</p>';
                    html += '<p>' + (articulo.cuerpo.length > 70 ? articulo.cuerpo.substring(0, 70) + '...' : articulo.cuerpo) + '</p>';

                    // Agrega los botones a cada artículo
                    html += '<div id="botoness">';
                    html += '<div style="display: inline-block;">';
                    html += '<form action="/editarArticulo" method="post">';
                    html += '<input type="hidden" name="idArticulo" value="' + articulo.id + '" />';
                    html += '<button type="submit" class="btn btn-primary">Editar</button>';
                    html += '</form>';
                    html += '</div>';

                    html += '<div style="display: inline-block;">';
                    html += '<form action="/agregarComentario" method="post">';
                    html += '<input type="hidden" name="idArticulo" value="' + articulo.id + '" />';
                    html += '<button type="submit" id="comment-button" class="btn btn-primary">Comentar</button>';
                    html += '</form>';
                    html += '</div>';

                    html += '<div style="display: inline-block;">';
                    html += '<form action="/eliminarArticulo" method="post">';
                    html += '<input type="hidden" name="idArticulo" value="' + articulo.id + '" />';
                    html += '<button type="submit" id="delete-button" style="background-color: red; color: white; padding: 5px 10px; border: none; cursor: pointer;">Eliminar</button>';
                    html += '</form>';
                    html += '</div>';

                    html += '</div>';
                }
                $('#ajaxx').html(html);
            } else {
                console.log('No se recibieron datos.');
            }
        });
    }


    $(document).on('click', '#pagination .page-link', function(e) {
        e.preventDefault();

        // Elimina la clase 'active' de cualquier otro enlace de página
        $('#pagination .page-link').removeClass('active');

        // Agrega la clase 'active' al enlace de página seleccionado
        $(this).addClass('active');

        var pagina = $(this).text();
        console.log('Página seleccionada:', pagina);
        cargarArticulos(pagina);
    });



    // Carga los artículos de la primera página al cargar la página inicialmente
    cargarArticulos(1);
});








