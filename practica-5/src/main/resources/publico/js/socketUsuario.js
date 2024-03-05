
var webSocket;
let nombreUser = document.getElementById("nombre").textContent.trim();
let params = new URLSearchParams(window.location.search)
let id = params.get('id')

$(document).ready(function () {

  conectar();

  $("#enviar").click(function () {
    let mensaje = $("#mensajeUser").val();
    webSocket.send(mensaje);
    insertarMensajeUsuario(mensaje);

    let input = document.getElementById("mensajeUser");
    input.value = "";
  });
});

function insertarMensajeUsuario(mensaje) {
  let nuevoMensaje = $("<div>").addClass("chat-message-right mb-4").append(
    $("<div>").addClass("flex-shrink-1 bg-light rounded py-2 px-3 mr-3").append(
      $("<div>").addClass("font-weight-bold mb-1").text(nombreUser),
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

function conectar() {
  webSocket = new WebSocket("ws://" + location.hostname + ":" + location.port + "/user-chat?nombre=" + nombreUser + "&id=" + id);
  console.log(id)

  //Cuando recibe mensaje del servidor (admin)
  webSocket.onmessage = function (event) {
    var message = event.data;
    if (message.endsWith("[ID]")) {

      // Este mensaje contiene el ID de sesión
      var sessionId = message.substring(0, message.length - "[ID]".length);

    } else {
      insertarMensajeServidor(message);
    }
  };

  webSocket.onopen = function () {
    console.log("Conectado - status " + this.readyState);
  };
  webSocket.onclose = function () {
    console.log("Desconectado - status " + this.readyState);
  };
}

