package org.example.servicios.grpc;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class GrpcServer {

	public static void main(String[] args) throws InterruptedException, IOException {
		Server server = ServerBuilder
				.forPort(8000)
				.addService(new UrlServiceImpl())
				.build();

		System.out.println("Servidor grpc iniciado en el puerto: " + server.getPort());
		server.start();
		server.awaitTermination();
	}
}
