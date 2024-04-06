$(document).ready(function(){
    $(".cargarContenido").click(function(e){
        e.preventDefault(); // Previene la acción predeterminada del enlace
        var pagina = $(this).attr('href'); // Obtiene la URL de la página vinculada
        $("#contenido").load(pagina); // Carga el contenido de la página en el div con el id "contenido"
    });
});
