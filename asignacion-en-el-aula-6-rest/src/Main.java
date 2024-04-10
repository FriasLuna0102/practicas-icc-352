import java.io.IOException;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;
import com.google.gson.Gson;

public class Main {

    private static URL urlApi;
    private static HttpRequest request;
    private static HttpResponse<String> response;
    private static final HttpClient cliente = HttpClient.newBuilder().build();

    public static void main(String[] args) throws IOException, URISyntaxException, InterruptedException {
        urlApi = new URI("http://localhost:7000/list/").toURL();
        Scanner scanner = new Scanner(System.in);
        boolean seguir = true;


        while (seguir){
            System.out.println("1. Listar Todos los estudiantes");
            System.out.println("2. Consultar Estudiantes");
            System.out.println("3. Crear un nuevo estudiante");
            System.out.println("4. Borrar un estudiante");
            System.out.println("5. Salir");
            System.out.print("Seleccione una opción: ");
            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option){
                case 1:
                    listarUrl(scanner.nextLine());
                    break;
                case 2:
                    System.out.print("Ingrese matricula: ");
                    consultarEstudiantes(scanner.nextLine());
                    break;
                case 3:
                    crearEstudiante();
                    break;
                case 4:
                    System.out.print("Ingrese matricula: ");
                    borrarEstudiante(scanner.nextLine());
                    break;
                case 5:
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

    public static void crearEstudiante() throws URISyntaxException, IOException, InterruptedException {
        Scanner scanner = new Scanner(System.in);
        int matricula;
        String nombre, carrera;
        System.out.print("Matricula: ");
        matricula = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Nombre: ");
        nombre = scanner.nextLine();
        System.out.print("Carrera: ");
        carrera = scanner.nextLine();

        Estudiante estudiante = new Estudiante(matricula,nombre,carrera);
        Gson gson = new Gson();

        request = HttpRequest.newBuilder()
                .uri(urlApi.toURI())
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(estudiante)))
                .build();

        response = cliente.send(request,HttpResponse.BodyHandlers.ofString());

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