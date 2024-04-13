package org.example;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.example.grpc.org.example.url_service.UrlServiceGrpc;
import org.example.grpc.org.example.url_service.UrlServiceOuterClass;

import java.util.List;

public class Main {
	public static void main(String[] args) {

		String host = "localhost";
		int puerto = 50051;

		ManagedChannel channel = ManagedChannelBuilder
				.forAddress(host, puerto)
				.usePlaintext()
				.build();

		UrlServiceGrpc.UrlServiceBlockingStub urlInterfaz = UrlServiceGrpc.newBlockingStub(channel);

		UrlServiceOuterClass.UrlListResponse urlListResponse = urlInterfaz
				.listUrls(UrlServiceOuterClass.UrlListRequest
						.newBuilder()
						.setUsername("randae")
						.build());


		imprimirUrlResponse(urlListResponse);
	}


	public static void imprimirUrlResponse(UrlServiceOuterClass.UrlListResponse urlListResponse){
		int count = urlListResponse.getUrlListList().size();
		List<UrlServiceOuterClass.EstadisticaURL> estadisticaURLList = urlListResponse.getEstadisticaUrlListList();
		List<UrlServiceOuterClass.ShortURL> urlList = urlListResponse.getUrlListList();

		for (int i = 0; i < count; i++){
			System.out.println("Codigo Url: " + urlList.get(i).getCodigo());
			System.out.println("Url Base: " + urlList.get(i).getUrlBase() );
			System.out.println("Url Corta: " + urlList.get(i).getUrlCorta());
			System.out.println("Cantidad de accesos: " + estadisticaURLList.get(i).getCantidadAccesos());
			System.out.println("Cantidad de direcciones ip: " + estadisticaURLList.get(i).getDireccionesIPCount());
			System.out.println("Cantidad de plataformas: " + estadisticaURLList.get(i).getPlataformasSOCount());
		}
	}
}