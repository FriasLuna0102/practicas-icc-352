package org.example;

import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


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


            System.out.println(response.headers());

            //Llamadas de metodos.
            NumeroDeLineas(contentType,response);
            cantParrafos(contentType,response);
            cantImg(contentType,response);
            cantFormularios(contentType, response);

            inputType(contentType,response);

            requestServer(contentType, response);

            System.out.println(response.headers().firstValue("matricula-id"));

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
            String content_length = response.headers().firstValue("Content-Length").orElse("Desconocido");
            System.out.println("Content Length: " + content_length);

        }
    }


    //2) Indicar la cantidad de párrafos (p) que contiene el documento HTML.
    public static void cantParrafos(String contentType, HttpResponse response) throws IOException {

        if (contentType.startsWith("text/html")) {

            // Utiliza Jsoup para parsear el HTML
            Document document = Jsoup.parse((String) response.body());

            // Selecciona todos los elementos de párrafo y cuenta cuántos hay
            Elements paragraphs = document.select("p");
            int numberOfParagraphs = paragraphs.size();

            System.out.println("Número de párrafos: " + numberOfParagraphs);
        }

    }



    //3. Indicar la cantidad de imágenes (img) dentro de los párrafos que
    //contiene el archivo HTML.
    public static void cantImg(String contentType, HttpResponse response) throws IOException {

        if (contentType.startsWith("text/html")) {

            // Utiliza Jsoup para parsear el HTML
            Document document = Jsoup.parse((String) response.body());

            // Selecciona todos los elementos de imagen y cuenta cuántos hay
            Elements img = document.select("img");
            int cantImg = img.size();

            System.out.println("Número de imagenes: " + cantImg);
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
                String method = forms.attr("method");
                if ("post".equalsIgnoreCase(form.attr("method"))){
                    postMethod++;
                } else if ("get".equalsIgnoreCase(form.attr("method"))) {
                    getMethod++;
                }
            }

            if(cantForm == 0){
                System.out.println("Este recurso no contiene Formularios.");
            }else {
                // Imprime el número total de formularios y cuántos usan POST y GET
                System.out.println("Número total de formularios: " + cantForm);
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

            // Itera sobre los formularios
            for (Element form : forms) {
                // Selecciona todos los elementos de entrada dentro del formulario
                Elements inputs = form.select("input");

                // Itera sobre los elementos de entrada
                for (Element input : inputs) {
                    // Obtiene el atributo 'type' de cada elemento de entrada
                    String  type = input.attr("type");
                    System.out.println("Campo de entrada encontrado con tipo: " + type);
                }
            }
        }
    }


   public static void requestServer(String contentType, HttpResponse response) throws IOException, URISyntaxException, InterruptedException {

        String url = String.valueOf(response.uri());

        if (contentType.startsWith("text/html")) {

            // Utiliza Jsoup para parsear el HTML
            Document document = Jsoup.parse((String) response.body());

            // Selecciona todos los elementos de formulario
            Elements form = document.select("form");


            for (Element forms : form) {
                String method = forms.attr("method");
                if ("post".equalsIgnoreCase(forms.attr("method"))) {

                    HttpClient client = HttpClient.newHttpClient();
                    HttpRequest request = HttpRequest.newBuilder().
                            header("matricula-id", "1014-3611").uri(new URI(url)).build();

                    HttpResponse<String> response2 = client.send(request, HttpResponse.BodyHandlers.ofString());


                    System.out.println(response2.headers());


            }

        }
    }


}


