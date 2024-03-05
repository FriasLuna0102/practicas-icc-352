
var websocket;
let nombreAdmin = document.getElementById("nombre").textContent.trim();

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
  //El id representara la ruta al chat del usuario
  let user = $("<a>").attr("href", "#").addClass("list-group-item list-group-item-action border-0").append(
    $("<div>").addClass("badge bg-success float-right").text("2"),
    $("<div>").addClass("d-flex align-items-start").append(
      $("<div>").addClass("flex-grow-1 ml-3").text(nombre)
    )
  );

  // Agregar el usuario a la lista
  $("#listaUsers").append(user);
}

function conectar() {
  webSocket = new WebSocket("ws://" + location.hostname + ":" + location.port + "/admin-chat?adminName=" + nombreAdmin);

  //indicando los eventos:
  webSocket.onmessage = function (data) {
    let mensaje = data.data

    if (mensaje.charAt(0) === '1') {
      let nombre = mensaje.substring(1)
      addUsuarioToLista(nombre, "") 
      
    }else{
      insertarMensajeServidor(mensaje)
    }
  };
  webSocket.onopen = function (e) {console.log("Conectado - status " + this.readyState);};
  webSocket.onclose = function (e) {
    console.log("Desconectado - status " + this.readyState);
  };
}

