function cargarComponente(event) {
    event.preventDefault(); // Prevenir la acción predeterminada del enlace

    // Ocultar el componente actual si está visible
    //document.getElementById('componentContainer').style.display = 'none';

    // Eliminar la clase activa de todos los enlaces
    var links = document.querySelectorAll('.nav-link');
    links.forEach(link => {
        link.classList.remove('active');
    });

    // Resaltar el enlace "Mis URL's"
    document.getElementById('misUrls').classList.add('active');

    // Realizar la solicitud GET al servidor para obtener el contenido del componente
    fetch('/url/misUrl')
        .then(response => response.text())
        .then(data => {
            // Insertar el contenido del componente en la página
            document.getElementById('componentContainer').innerHTML = data;
        })
        .catch(error => console.error('Error al cargar el componente:', error));
}

function cargarRegistroUsuario(event) {
    event.preventDefault(); // Prevenir la acción predeterminada del enlace

    // Ocultar el componente actual si está visible
    //document.getElementById('componentContainer').style.display = 'none';

    // Eliminar la clase activa de todos los enlaces
    var links = document.querySelectorAll('.nav-link');
    links.forEach(link => {
        link.classList.remove('active');
    });

    // Resaltar el enlace "Usuarios"
    document.getElementById('cargar-usuarios').classList.add('active');

    // Realizar la solicitud GET al servidor para obtener el contenido del componente
    fetch('/listarUsuarios')
        .then(response => response.text())
        .then(data => {
            // Insertar el contenido del componente en la página
            document.getElementById('componentContainer').innerHTML = data;
        })
        .catch(error => console.error('Error al cargar el componente:', error));
}
