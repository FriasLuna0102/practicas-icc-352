package org.example.servicios.mongo;

import com.mongodb.client.MongoClients;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import dev.morphia.query.Query;
import org.example.encapsulaciones.ShortURL;

public class URLODM {

	private static URLODM instance;
	private final Datastore datastore;

	public static URLODM getInstance(){
		if (instance == null){
			instance = new URLODM();
		}
		return instance;
	}
	private URLODM() {
		ProcessBuilder processBuilder = new ProcessBuilder();
		String URL_MONGODB = processBuilder.environment().get("URL_MONGO");
		String DB_NOMBRE = processBuilder.environment().get("DB_NOMBRE");

		datastore = Morphia.createDatastore(MongoClients.create(URL_MONGODB), DB_NOMBRE);
		datastore.getMapper().map(ShortURL.class);
		datastore.ensureIndexes();
	}

	public void guardarURL(ShortURL shortURL){
		datastore.save(shortURL);
	}

	public ShortURL buscarUrlByUrlLarga(String urlLarga){
		Query<ShortURL> shortURLS = datastore.find(ShortURL.class).filter("urlBase", urlLarga);

		return shortURLS.first();
	}

	public String buscarUrlByCodigo(String codigo){
		Query<ShortURL> url = datastore.find(ShortURL.class).filter("codigo", codigo);

		return url.first().getUrlBase();
	}
}
