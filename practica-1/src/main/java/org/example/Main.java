package org.example;

import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class Main {

    //a) Indicar el tipo de recurso que estamos seleccionando, es decir, si es
    //un documento HTML, PDF, Imágenes, entre otros.

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

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // Obtener el tipo de contenido del recurso desde los encabezados
            String contentType = response.headers().firstValue("Content-Type").orElse("Desconocido");

            // Imprimir el tipo de recurso
            System.out.println("Tipo de recurso seleccionado: " + contentType);



            NumeroDeLineas(contentType,response);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    //1) Indicar la cantidad de lineas del recurso retornado.

    public static void NumeroDeLineas(String contentType,HttpResponse response) throws IOException {

        if (contentType.startsWith("text/html")) {

            // Utilizar un BufferedReader para contar las líneas del HTML
            BufferedReader htmlReader = new BufferedReader(new StringReader((String) response.body()));
            int numberOfLines = 0;
            while (htmlReader.readLine() != null) {
                numberOfLines++;
            }
            htmlReader.close();

            // Imprime el número de líneas
            System.out.println("Número de líneas: " + numberOfLines);

        }
    }

    //2) Indicar la cantidad de párrafos (p) que contiene el documento HTML.



}


