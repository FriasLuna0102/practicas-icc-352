// Declarar base de datos
const db = new Dexie("FormularioDB");
db.version(1).stores({
  registros: "++id, nombre, sector, nivelEscolar, altitud, longitud, usuario, estado",
  usuarios: "&username, nombre, password, rol"
});

window.db = db
