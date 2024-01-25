package org.example;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import io.javalin.Javalin;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import static org.eclipse.jetty.http.HttpURI.build;


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
            System.out.println(" ");
            System.out.println("Tipo de recurso seleccionado: " + contentType);
            System.out.println(" ");


            System.out.println("Codigo de respuesta: "+ response.statusCode());

            //Llamadas de metodos.
            NumeroDeLineas(contentType,response);
            cantParrafos(contentType,response);
            cantFormularios(contentType, response);

            inputType(contentType,response);

            requestServer(contentType,response);



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void servicioJavalin (){

        Javalin app = Javalin.create();
        app.start(8080);
        app.get("/get", ctx -> ctx.result("Hello world"));


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
            String content_length = response.headers().firstValue("Content-Length").orElse("Desconocido");
            System.out.println("Content Length: " + content_length);

        }
    }


    //2) Indicar la cantidad de párrafos (p) que contiene el documento HTML.

    //3. Indicar la cantidad de imágenes (img) dentro de los párrafos que
    //contiene el archivo HTML.
    public static void cantParrafos(String contentType, HttpResponse response) throws IOException {

        if (contentType.startsWith("text/html")) {

            // Utiliza Jsoup para parsear el HTML
            Document document = Jsoup.parse((String) response.body());

            // Selecciona todos los elementos de párrafo y cuenta cuántos hay
            Elements paragraphs = document.select("p");
            int numberOfParagraphs = paragraphs.size();

            System.out.println("Número de párrafos: " + numberOfParagraphs);

            int cantImg = 0;

            for(Element parraf : paragraphs){
                Elements imagenes = parraf.select("img");
                cantImg += imagenes.size();
            }

            System.out.println("Cantidad de imagenes dentro de los parrafos: "+cantImg);
        }

    }





    //4. indicar la cantidad de formularios (form) que contiene el HTML por
    //categorizando por el método implementado POST o GET.

    public static void cantFormularios(String contentType, HttpResponse response) throws IOException {

        if (contentType.startsWith("text/html")) {

            // Utiliza Jsoup para parsear el HTML
            Document document = Jsoup.parse((String) response.body());

            // Selecciona todos los elementos de formularios y cuenta cuántos hay
            Elements form = document.select("form");
            int cantForm = form.size();

            int postMethod = 0;
            int getMethod = 0;

            for (Element forms : form) {
                if (forms.attr("method").equalsIgnoreCase("post")){
                    postMethod++;
                } else if (forms.attr("method").equalsIgnoreCase("get")) {
                    getMethod++;
                }
            }

            if(cantForm == 0){
                System.out.println("Este recurso no contiene Formularios.");
            }else {
                // Imprime el número total de formularios y cuántos usan POST y GET
                System.out.println("Número de formularios que usan POST: " + postMethod);
                System.out.println("Número de formularios que usan GET: " + getMethod);
            }

        }
    }


    //5)Para cada formulario mostrar los campos del tipo input y su
    //respectivo tipo que contiene en el documento HTML.

    public static void inputType(String contentType, HttpResponse response) throws IOException {
        if (contentType.startsWith("text/html")) {
            // Utiliza Jsoup para parsear el HTML
            Document document = Jsoup.parse((String) response.body());

            // Selecciona todos los elementos de formulario
            Elements forms = document.select("form");
            int cantForms = forms.size();

            if(cantForms != 0){

                System.out.println("Tipos de Input:");
                // Itera sobre los formularios
                for (Element form : forms) {
                    // Selecciona todos los elementos de entrada dentro del formulario
                    Elements inputs = form.select("input");

                    // Itera sobre los elementos de entrada
                    for (Element input : inputs) {
                        // Obtiene el atributo 'type' de cada elemento de entrada
                        String  type = input.attr("type");
                        System.out.println(type);

                    }
                }
            }

        }
    }


    //Mandar parametro al action que contiene los formularios.
    public static void requestServer(String contentType, HttpResponse response) throws IOException, URISyntaxException, InterruptedException {

        if (contentType.startsWith("text/html")) {

            // Utiliza Jsoup para parsear el HTML
            Document document = Jsoup.parse((String) response.body());

            // Selecciona todos los elementos de formulario
            Elements form = document.select("form");

            // URL del servidor y parámetros
            String url = String.valueOf(response.uri());
            String asignatura = "practica1";
            String matriculaId = "1014";



            for (Element forms : form) {

                String actionUrl = forms.attr("action");
                String originalUrl = String.valueOf(response.uri());
                int longitud = originalUrl.length();

                if(longitud > 0){
                    char ultimoCaracter = originalUrl.charAt(longitud - 1);

                    if(ultimoCaracter == '/'){
                        originalUrl = originalUrl.substring(0, originalUrl.length() - 1);
                    }
                }

                if (!actionUrl.contains("/")) {
                    // Obtener la URL original del recurso
                    actionUrl = "/"+actionUrl;
                }

                URI uri = URI.create(originalUrl + actionUrl + "?asignatura=" + asignatura);

                if (forms.attr("method").equalsIgnoreCase("post")) {


                    // Crear la solicitud HTTP con el método POST
                    HttpRequest request = HttpRequest.newBuilder()
                            .uri(URI.create(String.valueOf(uri)))
                            .header("matricula-id", matriculaId)
                            .POST(HttpRequest.BodyPublishers.noBody())
                            .build();

                    // Crear el cliente HTTP
                    HttpClient client = HttpClient.newHttpClient();

                    // Enviar la solicitud y obtener la respuesta
                    HttpResponse<String> response2 = client.send(request, HttpResponse.BodyHandlers.ofString());

                    System.out.println("Código de respuesta: " + response2.statusCode());
                    System.out.println("El metodo utilizado para la peticion es: "+ request.method());
                    System.out.println(uri);
                }

            }

        }
    }
}


