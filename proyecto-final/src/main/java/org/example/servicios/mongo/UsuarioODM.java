package org.example.servicios.mongo;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoClients;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import dev.morphia.query.Query;
import dev.morphia.query.experimental.filters.Filter;
import org.example.encapsulaciones.Usuario;

public class UsuarioODM {

	private final Datastore datastore;

	public UsuarioODM(){

		ProcessBuilder processBuilder = new ProcessBuilder();
		String URL_MONGODB = processBuilder.environment().get("URL_MONGO");
		String DB_NOMBRE = processBuilder.environment().get("DB_NOMBRE");

		datastore = Morphia.createDatastore(MongoClients.create(URL_MONGODB), DB_NOMBRE);
		datastore.getMapper().map(Usuario.class);
		datastore.ensureIndexes();
	}

	public void guardarUsuario(Usuario usuario){
		datastore.save(usuario);
	}

	public Usuario buscarUsuarioByUsername(String username){
		Query<Usuario> usuarios = datastore.find(Usuario.class).filter("username",username);

		return usuarios.first();
	}
}
