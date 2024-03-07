let idSession;
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
    let url = '/historial?websocket=' + $(this).attr('href') + "&idSessionAdmin=" + idSession;


    // Realizar una solicitud AJAX para obtener el contenido de la página
    $.ajax({
      url: url,
      type: 'GET',
      success: function(response) {

        // Actualizar el contenido del div "chat" con el contenido modificado
        $('#chat').html(response);
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

  websocket.send(nuevoMensaje[0].outerHTML)
  //$("#chat").append(nuevoMensaje);
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

  $("#chat").append(mensaje);
}
function addUsuarioToLista(nombre, session) {
  // El id representará la ruta al chat del usuario
  let user = $("<button>").attr("href", session).addClass("list-group-item list-group-item-action border-0 chatter").append(
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
      let session = mensaje.substring(separador + 1);
      
      addUsuarioToLista(nombre, session)
      
    }
    if (mensaje.endsWith("[@#Id#@]")) {
        idSession = mensaje.slice(0, mensaje.indexOf("[@#Id#@]"))
    } else{
      insertarMensajeServidor(mensaje)
    }
  };
  webSocket.onopen = function (e) {console.log("Conectado - status " + this.readyState);};
  webSocket.onclose = function (e) {
    console.log("Desconectado - status " + this.readyState);
  };
}

