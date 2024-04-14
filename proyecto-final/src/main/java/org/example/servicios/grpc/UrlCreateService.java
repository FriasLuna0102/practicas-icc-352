package org.example.servicios.grpc;

import io.grpc.stub.StreamObserver;
import org.example.encapsulaciones.EstadisticaURL;
import org.example.encapsulaciones.ShortURL;
import org.example.encapsulaciones.Usuario;
import org.example.grpc.org.example.url_service.UrlServiceGrpc;
import org.example.grpc.org.example.url_service.UrlServiceOuterClass;
import org.example.servicios.mongo.EstadisticaODM;
import org.example.servicios.mongo.URLODM;
import org.example.servicios.mongo.UsuarioODM;

public class UrlCreateService extends UrlServiceGrpc.UrlServiceImplBase {

	//@Override
	public void createUrl(UrlServiceOuterClass.UrlCreateRequest request, StreamObserver<UrlServiceOuterClass.UrlCreateResponse> responseStreamObserver){
		String username = request.getUsername();
		String urlRequest = request.getUrlBase();
		Usuario usuario = UsuarioODM.getInstance().buscarUsuarioByUsername(username);
		ShortURL shortURL = URLODM.getInstance().buscarUrlByUrlLarga(urlRequest);
		EstadisticaURL estadisticaUrl = new EstadisticaURL();

		// Manejar logica en caso de que usuario no exista
		if (usuario == null){

		}
		// Manejar logica en caso de la url
		if (shortURL == null){
			shortURL = new ShortURL(urlRequest, null);
			URLODM.getInstance().guardarURL(shortURL);
			estadisticaUrl = EstadisticaODM.getInstance().buscarEstadisticaByCodigoOfUrl(shortURL.getCodigo());
		}else {


		}



		// Construir la respuesta
		UrlServiceOuterClass.UrlCreateResponse.Builder responseBuilder = UrlServiceOuterClass.UrlCreateResponse.newBuilder()
				.setShortUrl(UrlServiceImpl.convertirUrl(shortURL))
				.setEstadisticaUrl(UrlServiceImpl.convertirEstadistica(estadisticaUrl));

		responseStreamObserver.onNext(responseBuilder.build());
		responseStreamObserver.onCompleted();
	}

	private void construirUrl(ShortURL shortURL, String urlBase,EstadisticaURL estadisticaURL){
		shortURL = new ShortURL(urlBase, null);
		URLODM.getInstance().guardarURL(shortURL);
		estadisticaURL = EstadisticaODM.getInstance().buscarEstadisticaByCodigoOfUrl(shortURL.getCodigo());
	}
}
