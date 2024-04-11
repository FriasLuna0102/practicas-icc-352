package org.example;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class Main {

    private static URL urlApi;
    private static URL urlApiCrear;

    private static HttpRequest request;
    private static HttpResponse<String> response;
    private static final HttpClient cliente = HttpClient.newBuilder().build();

    public static void main(String[] args) throws IOException, URISyntaxException, InterruptedException {
        urlApi = new URI("http://localhost:7000/list/").toURL();
        urlApiCrear = new URI("http://localhost:7000/generar").toURL();
        Scanner scanner = new Scanner(System.in);
        boolean seguir = true;


        while (seguir){
            System.out.println("1. Listado de URL de un Usuario");
            System.out.println("2. Crear un Registro de URL");
            System.out.println("3. Salir");
            System.out.print("Seleccione una opción: ");
            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option){
                case 1:
                    System.out.println("Ingrese el nombre del usuario:");
                    listarUrl(scanner.nextLine());
                    break;
                case 2:
                    crearRegistroUrl();
                    break;

                case 3:
                    seguir = false;
            }
        }
    }

    public static void listarEstudiantes(HttpResponse<String> response) throws IOException, InterruptedException, URISyntaxException {
        request = HttpRequest.newBuilder()
                .uri(urlApi.toURI())
                .build();

        response = cliente.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
    }

    public static void listarUrl(String nombre) throws IOException, InterruptedException, URISyntaxException {
        request = HttpRequest.newBuilder()
                .uri(URI.create(urlApi.toURI() + nombre))
                .build();

        response = cliente.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
    }

    public static void consultarEstudiantes(String matricula) throws URISyntaxException, IOException, InterruptedException {
        request = HttpRequest.newBuilder()
                .uri(URI.create(urlApi.toURI() + matricula))
                .build();
        response = cliente.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
    }

    public static void crearRegistroUrl() throws URISyntaxException, IOException, InterruptedException {
        Scanner scanner = new Scanner(System.in);

        String nombre, urlBase;
        System.out.print("Nombre de Usuario: ");
        nombre = scanner.nextLine();
        System.out.print("URL a acortar: ");
        urlBase = scanner.nextLine();

        // Construir un objeto JSON con el nombre de usuario y la URL
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("nombre", nombre);
        jsonObject.addProperty("urlBase", urlBase);

        Gson gson = new Gson();

        // Enviar la solicitud POST con los datos JSON
        request = HttpRequest.newBuilder()
                .uri(urlApiCrear.toURI())
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(jsonObject)))
                .build();

        response = cliente.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("Respuesta del servidor: " + response.body());
    }


    public static void borrarEstudiante(String matricula) throws URISyntaxException, IOException, InterruptedException {

        request = HttpRequest.newBuilder()
                .uri(URI.create(urlApi.toURI() + matricula))
                .DELETE()
                .build();

        response = cliente.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println(response.body());
    }
}