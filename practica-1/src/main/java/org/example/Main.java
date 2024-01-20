package org.example;

import io.javalin.Javalin;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {
            // Utilizar Scanner para leer la URL desde la consola
            Scanner scanner = new Scanner(System.in);
            System.out.println("Ingrese una URL válida:");
            String url = scanner.nextLine();

            // Crear un cliente HTTP y realizar la solicitud
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .build();

            HttpResponse<Void> response = httpClient.send(request, HttpResponse.BodyHandlers.discarding());

            // Obtener el tipo de contenido del recurso desde los encabezados
            String contentType = response.headers().firstValue("Content-Type").orElse("Desconocido");

            // Imprimir el tipo de recurso
            System.out.println("Tipo de recurso seleccionado: " + contentType);

        } catch (Exception e) {
            e.printStackTrace();
        }



    /*
        var app = Javalin.create()
                .get("/", ctx -> ctx.result("Hello World Starlin"))
                .start(7070);
     */
    }
}