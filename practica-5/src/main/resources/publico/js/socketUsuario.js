
var websocket;


$(document).ready(function () {
  console.info("Iniciando Jquery -  Ejemplo WebServices");

  conectar();

  $("#enviar").click(function () {
    webSocket.send($("#mensajeUser").val());
    let input = document.getElementById("mensajeUser");
    input.value = "";
  });
});

function conectar() {
  webSocket = new WebSocket("ws://" + location.hostname + ":" + location.port + "/user-chat");

  //indicando los eventos:
  webSocket.onmessage = function (data) {recibirInformacionServidor(data);};
  webSocket.onopen = function (e) {console.log("Conectado - status " + this.readyState);};
  webSocket.onclose = function (e) {
    console.log("Desconectado - status " + this.readyState);
  };
}

