
var webSocket;
let nombreUser = document.getElementById("nombre").textContent.trim();
let ruta = window.location.href
let params = new URLSearchParams(window.location.search)
let id = params.get('id')

$(document).ready(function () {

  conectar();

  $("#enviar").click(function () {
    let mensaje = $("#mensajeUser").val();
    insertarMensajeUsuario(mensaje);

    let input = document.getElementById("mensajeUser");
    input.value = "";
  });
});

let mensajesCargados = {};

function insertarMensajeUsuario(mensaje) {
    // Verificar si el mensaje ya ha sido cargado
    if (mensajesCargados[mensaje]) {
        return;
    }

    // Marcar el mensaje como cargado
    mensajesCargados[mensaje] = true;

    let nuevoMensaje = $("<div>").addClass("chat-message-right mb-4").append(
        $("<div>").addClass("flex-shrink-1 bg-light rounded py-2 px-3 mr-3").append(
            $("<div>").addClass("font-weight-bold mb-1").text(nombreUser),
            $("<p>").text(mensaje)
        )
    );

    webSocket.send(nuevoMensaje[0].outerHTML);
    $("#chat").append(nuevoMensaje);
}
function limpiarChatAdmin() {
    // Limpiar el chat del administrador
    $("#listaUsers").empty();
}

let i = 0;
function conectar() {
    webSocket = new WebSocket("ws://" + location.hostname + ":" + location.port + "/user-chat?nombre=" + nombreUser + "&ruta=" + ruta);


    console.log("Se llamo", i++);
    //Cuando recibe mensaje del servidor (admin)
    webSocket.onmessage = function (event) {
        var message = event.data;
        console.log(message);
        if (message.endsWith("[ID]")) {

            // Este mensaje contiene el ID de sesión
            var sessionId = message.substring(0, message.length - "[ID]".length);

        } else {
            // Solo agregar el mensaje al chat si no ha sido cargado
            if (!mensajesCargados[message]) {
                $("#chat").append(message);
            }
        }
    };

    webSocket.onopen = function () {
        console.log("Conectado - status " + this.readyState);
    };
    webSocket.onclose = function () {
        console.log("Desconectado - status " + this.readyState);
        // Reconectar después de un retraso
        limpiarChatAdmin();

        conectar();
    };
}




