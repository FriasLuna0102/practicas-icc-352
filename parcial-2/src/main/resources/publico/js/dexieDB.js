import Dexie from 'dexie';

// Declarar base de datos
const db = new Dexie("FormularioDB");
db.version(1).stores({
  registros: "++id, nombre, sector, nivelEscolar, altitud, longitud",
  usuarios: "&username, nombre, password"
});

export default db;
