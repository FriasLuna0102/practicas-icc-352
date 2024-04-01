package org.example.servicios.mongo;

import com.mongodb.client.MongoClients;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import dev.morphia.query.Query;
import org.example.encapsulaciones.ShortURL;
import org.example.encapsulaciones.Visitante;

public class VisitanteODM {
	private static VisitanteODM instance;
	private final Datastore datastore;

	public static VisitanteODM getInstance(){
		if (instance == null){
			instance = new VisitanteODM();
		}
		return instance;
	}
	private VisitanteODM() {
		ProcessBuilder processBuilder = new ProcessBuilder();
		String URL_MONGODB = processBuilder.environment().get("URL_MONGO");
		String DB_NOMBRE = processBuilder.environment().get("DB_NOMBRE");

		datastore = Morphia.createDatastore(MongoClients.create(URL_MONGODB), DB_NOMBRE);
		datastore.getMapper().map(Visitante.class);
		datastore.ensureIndexes();
	}

	public void guardarVisitante(Visitante visitante){
		datastore.save(visitante);
	}

	public Visitante buscarVisitanteById(String id){
		Query<Visitante> visitanteQuery = datastore.find(Visitante.class).filter("_id", id);

		return visitanteQuery.first();
	}

}
