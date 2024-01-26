package org.example;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import io.javalin.Javalin;
import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
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
            String matriculaId = "1014-3611";

            for (Element forms : form) {

                String actionUrl = forms.attr("action");

                if (!actionUrl.contains("/")) {
                    // Obtener la URL original del recurso
                    actionUrl = "/"+actionUrl;
                }

                URI originalUrl = URI.create(String.valueOf(response.uri()));
                URI newUri = originalUrl.resolve(actionUrl);

                if (forms.attr("method").equalsIgnoreCase("post")) {

                    CloseableHttpClient client = HttpClients.createDefault();
                    HttpPost httpPost = new HttpPost(newUri);

                    // Agregar encabezados
                    httpPost.addHeader("matricula-id", matriculaId);
                    httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");

                    List<NameValuePair> params = new ArrayList<>();
                    params.add(new BasicNameValuePair("asignatura", "practica1"));
                    httpPost.setEntity(new UrlEncodedFormEntity(params, StandardCharsets.UTF_8));

                    CloseableHttpResponse response2 = client.execute(httpPost);

                    // Enviar la solicitud y obtener la respuesta
                    System.out.println("Código de respuesta del servidor: " + response2.getStatusLine().getStatusCode());
                    System.out.println("El metodo utilizado para la peticion es: "+ httpPost.getMethod());
                    System.out.println(response2);

                    Header header = httpPost.getFirstHeader("matricula-id");
                    System.out.println("El header enviado: "+header.getName() + ": " + header.getValue());

                    NameValuePair usuarioParam = params.stream()
                            .filter(param -> param.getName().equals("asignatura"))
                            .findFirst()
                            .orElse(null);
                    if (usuarioParam != null) {
                        System.out.println("El parametro enviado: "+ usuarioParam.getName() +"=" +usuarioParam.getValue());

                    System.out.println("URL a la que fue enviada la peticion post: " + newUri);

                    }
                }

            }

        }
    }
}


