package org.example;

import io.javalin.Javalin;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.util.Scanner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) throws IOException {
        try {
            // Utilizar BufferedReader para leer la URL desde la consola
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Ingrese una URL válida:");
            String url = reader.readLine();

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


            if(contentType.startsWith("text/html")){
                HttpResponse<String> response2 = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                String body = response2.body();

                String[] lines = body.split("\n");
                int numberOfLines = lines.length;
                System.out.println("Número de líneas: " + numberOfLines);
                System.out.println(response2.headers());

            }

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