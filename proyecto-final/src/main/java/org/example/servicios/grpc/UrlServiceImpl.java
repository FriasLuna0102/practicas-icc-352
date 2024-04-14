package org.example.servicios.grpc;

import com.google.protobuf.Timestamp;
import io.grpc.stub.StreamObserver;
import org.example.encapsulaciones.EstadisticaURL;
import org.example.encapsulaciones.ShortURL;
import org.example.encapsulaciones.Usuario;
import org.example.grpc.org.example.url_service.UrlServiceGrpc;
import org.example.grpc.org.example.url_service.UrlServiceOuterClass;
import org.example.servicios.mongo.EstadisticaODM;
import org.example.servicios.mongo.UsuarioODM;

import java.util.*;

public class UrlServiceImpl extends UrlServiceGrpc.UrlServiceImplBase {

	@Override
	public void listUrls(UrlServiceOuterClass.UrlListRequest request, StreamObserver<UrlServiceOuterClass.UrlListResponse> responseStreamObserver){
		String username = request.getUsername();
		List<ShortURL> shortURLList = new ArrayList<>();
		List<EstadisticaURL> estadisticaURLList = new ArrayList<>();

		Usuario usuario = UsuarioODM.getInstance().buscarUsuarioByUsername(username);
		shortURLList = usuario.getUrlList();
		obtenerListEstadisticas(estadisticaURLList, shortURLList);

		List<UrlServiceOuterClass.ShortURL> shortUrlResponseList = new ArrayList<>();
		List<UrlServiceOuterClass.EstadisticaURL> estadisticaUrlResponseList = new ArrayList<>();

		for (ShortURL url : shortURLList){
			shortUrlResponseList.add(convertirUrl(url));
		}

		for (EstadisticaURL estadisticaURL : estadisticaURLList){
			estadisticaUrlResponseList.add(convertirEstadistica(estadisticaURL));
		}

		// Construir la respuesta
		UrlServiceOuterClass.UrlListResponse.Builder responseBuilder = UrlServiceOuterClass.UrlListResponse.newBuilder()
				.addAllUrlList(shortUrlResponseList)
				.addAllEstadisticaUrlList(estadisticaUrlResponseList);

		// Enviar la respuesta al cliente
		responseStreamObserver.onNext(responseBuilder.build());
		responseStreamObserver.onCompleted();
	}


	public void obtenerListEstadisticas(List<EstadisticaURL> listaEstadisticas, List<ShortURL> urls){

		EstadisticaURL aux;

		for (ShortURL url : urls) {
			aux = EstadisticaODM.getInstance().buscarEstadisticaByCodigoOfUrl(url.getCodigo());
			listaEstadisticas.add(aux);
		}

	}

	public static UrlServiceOuterClass.ShortURL convertirUrl(ShortURL shortURL){
		return UrlServiceOuterClass.ShortURL.newBuilder()
				.setId(shortURL.getId())
				.setCodigo(shortURL.getCodigo())
				.setUrlBase(shortURL.getUrlBase())
				.setUrlCorta(shortURL.getUrlCorta())
				.setFechaCreacion(convertirAfechaProto(shortURL.getFechaCreacion()))
				.build();
	}

	public static Timestamp convertirAfechaProto(Date fecha){

		long segundos = fecha.getTime() / 1000; // Convertir milisegundos a segundos
		int nanosegundos = (int) ((fecha.getTime() % 1000) * 1000000); // Obtener los nanosegundos

		return Timestamp.newBuilder()
				.setSeconds(segundos)
				.setNanos(nanosegundos)
				.build();
	}

	public static UrlServiceOuterClass.EstadisticaURL convertirEstadistica(EstadisticaURL estadisticaURL){

		UrlServiceOuterClass.EstadisticaURL.Builder builder = UrlServiceOuterClass.EstadisticaURL
				.newBuilder()
				.setId(String.valueOf(estadisticaURL.getId()))
				.setCantidadAccesos(estadisticaURL.getCantidadAccesos());

		for (Map.Entry<String, Integer> entry : estadisticaURL.getNavegadores().entrySet()) {
			builder.putNavegadores(entry.getKey(), entry.getValue());
		}

		for (Map.Entry<String, Integer> entry : estadisticaURL.getDireccionesIP().entrySet()) {
			builder.putDireccionesIP(entry.getKey(), entry.getValue());
		}

		for (Map.Entry<String, Integer> entry : estadisticaURL.getDominiosClientes().entrySet()) {
			builder.putDominiosClientes(entry.getKey(), entry.getValue());
		}

		for (Map.Entry<String, Integer> entry : estadisticaURL.getPlataformasSO().entrySet()) {
			builder.putPlataformasSO(entry.getKey(), entry.getValue());
		}

		for (Map.Entry<String, Integer> entry : estadisticaURL.getHorasAcceso().entrySet()) {
			builder.putHorasAcceso(entry.getKey(), entry.getValue());
		}
		builder.setShortURL(convertirUrl(estadisticaURL.getShortURL()));


		return builder.build();
	}
}
