document.addEventListener("DOMContentLoaded", function() {
  const db = window.db;

  // Función para insertar un registro en la base de datos Dexie
  async function insertarRegistro() {
    console.log("Entrando a funcion")
    try {
      let username = document.getElementById("username").value;
      let nombre = document.getElementById("nombre").value;
      let password = document.getElementById("password").value;
      let rolesSeleccionados = document.getElementById("roles").value;

      // Crear un objeto de registro con los valores obtenidos
      let registro = {
        nombre: nombre,
        username: username,
        password: password,
        roles: rolesSeleccionados
      };

      await db.usuarios.add(registro);
      console.log("Registrar usuario exitoso");
      console.log(registro)
    } catch (error) {
      console.error("Error en registro:", error);
    }
  }

  function handleClick() {
    insertarRegistro();
  }

  const botonGuardar = document.getElementById("form-send");

  botonGuardar.addEventListener("click", handleClick);
});
