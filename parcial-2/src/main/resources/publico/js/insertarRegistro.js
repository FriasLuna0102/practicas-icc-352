document.addEventListener("DOMContentLoaded", function() {
  const db = window.db;

  // Función para insertar un registro en la base de datos Dexie
  async function insertarRegistro() {
    console.log("Entrando a funcion")
    try {
      let nombre = document.getElementById("nombre").value;
      let sector = document.getElementById("sector").value;
      let nivelEscolar = document.getElementById("nivelEscolar").value;
      let altitud = document.getElementById("altitud").value;
      let longitud = document.getElementById("longitud").value;
      let usuario = document.getElementById("usuario").value;

      // Crear un objeto de registro con los valores obtenidos
      let registro = {
        nombre: nombre,
        sector: sector,
        nivelEscolar: nivelEscolar,
        altitud: altitud,
        longitud: longitud,
        usuario: usuario,
        estado: false
      };

      await db.registros.add(registro);
      console.log("Registro exitoso");
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
