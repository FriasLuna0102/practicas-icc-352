package org.example.servicios.mongo;

import com.mongodb.client.MongoClients;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import dev.morphia.query.Query;
import org.example.encapsulaciones.Usuario;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class UsuarioODM {

	private static UsuarioODM instance;
	private final Datastore datastore;

	public static UsuarioODM getInstance(){
		if (instance == null){
			instance = new UsuarioODM();
		}
		return instance;
	}

	private UsuarioODM(){

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

    public List<Usuario> buscarTodosLosUsuarios(){
        List<Usuario> usuarios = new ArrayList<>();
        Iterator<Usuario> iterator = datastore.find(Usuario.class).iterator();
        while(iterator.hasNext()){
            usuarios.add(iterator.next());
        }
        return usuarios;
    }



}
