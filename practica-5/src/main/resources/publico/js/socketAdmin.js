
var websocket;
let nombreAdmin = document.getElementById("nombre").textContent.trim();
let params = new URLSearchParams(window.location.href);
let id = params.get('id')

$(document).ready(function () {
  console.info("Iniciando Jquery -  Ejemplo WebServices");

  conectar();

  $("#enviar").click(function () {
    let mensaje = $("#mensajeAdmin").val();
    webSocket.send(mensaje);
    insertarMensajeAdmin(mensaje);

    let input = document.getElementById("mensajeAdmin");
    input.value = "";
  });

  $(document).on('click', '.chatter', function(event) {
    // Evitar el comportamiento predeterminado del enlace
    event.preventDefault();

    // Obtener la URL de la página desde el atributo "data-url" del enlace
    var url = $(this).attr('href');

    // Realizar una solicitud AJAX para obtener el contenido de la página
    $.ajax({
      url: url,
      type: 'GET',
      success: function(response) {
        // Obtener el contenido del div "chat" del response
        var chatContent = $(response).find('#chat');

        // Modificar las clases de los elementos dentro del div "chat"
        chatContent.find('.chat-message-left').toggleClass('chat-message-left chat-message-right');
        chatContent.find('.chat-message-right').toggleClass('chat-message-right chat-message-left');

        // Actualizar el contenido del div "chat" con el contenido modificado
        $('#chat').html(chatContent.html());
        console.log(response)
      },
      error: function(xhr, status, error) {
        // Manejar errores si la solicitud AJAX falla
        console.error(error);
      }
    });
  });

});

function insertarMensajeAdmin(mensaje) {
  let nuevoMensaje = $("<div>").addClass("chat-message-right mb-4").append(
    $("<div>").addClass("flex-shrink-1 bg-light rounded py-2 px-3 mr-3").append(
      $("<div>").addClass("font-weight-bold mb-1").text(nombreAdmin),
      $("<p>").text(mensaje)
    )
  );

  $("#chat").append(nuevoMensaje);
}

function insertarMensajeServidor(mensaje) {
  let data = mensaje.split(",");
  
  let info = data[0].trim();
  let nombre = data[1].trim();

  let nuevoMensaje = $("<div>").addClass("chat-message-left mb-4").append(
    $("<div>").addClass("flex-shrink-1 bg-light rounded py-2 px-3 mr-3").append(
      $("<div>").addClass("font-weight-bold mb-1").text(nombre),
      $("<p>").text(info)
    )
  );

  $("#chat").append(nuevoMensaje);
}
function addUsuarioToLista(nombre, rutaChatUsuario) {
  // El id representará la ruta al chat del usuario
  let user = $("<button>").attr("href", rutaChatUsuario).addClass("list-group-item list-group-item-action border-0 chatter").append(
      $("<div>").addClass("badge bg-success float-right").text("2"),
      $("<div>").addClass("d-flex align-items-start").append(
          $("<div>").addClass("flex-grow-1 ml-3").text(nombre)
      )
  );

  // Agregar el usuario a la lista
  $("#listaUsers").append(user);
}

function conectar() {
  webSocket = new WebSocket("ws://" + location.hostname + ":" + location.port + "/admin-chat?adminName=" + nombreAdmin + "&id=" + id);

  //indicando los eventos:
  webSocket.onmessage = function (data) {
    let mensaje = data.data

    if (mensaje.charAt(0) === '1') {
      let separador = mensaje.indexOf(':');
      let nombre = mensaje.substring(1, separador);
      let rutaAchat = mensaje.substring(separador + 1);
      
      addUsuarioToLista(nombre, rutaAchat) 
      
    }else{
      insertarMensajeServidor(mensaje)
    }
  };
  webSocket.onopen = function (e) {console.log("Conectado - status " + this.readyState);};
  webSocket.onclose = function (e) {
    console.log("Desconectado - status " + this.readyState);
  };
}

