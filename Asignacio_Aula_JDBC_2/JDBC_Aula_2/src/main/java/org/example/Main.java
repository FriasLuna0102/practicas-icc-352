package org.example;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.javalin.openapi.JsonSchemaLoader;
import io.javalin.openapi.JsonSchemaResource;
import io.javalin.openapi.OpenApi;
import io.javalin.openapi.plugin.OpenApiPlugin;
import io.javalin.openapi.plugin.OpenApiPluginConfiguration;
import io.javalin.openapi.plugin.redoc.ReDocConfiguration;
import io.javalin.openapi.plugin.redoc.ReDocPlugin;
import io.javalin.openapi.plugin.swagger.SwaggerConfiguration;
import io.javalin.openapi.plugin.swagger.SwaggerPlugin;
import org.example.controladores.CrudTradicionalControlador;


import java.text.SimpleDateFormat;
import java.util.Date;

public class Main {


    public static void main(String[] args) {
        //Ejemplo hola mundo
        String mensaje = "Hola Mundo en Javalin :-D";
        System.out.println(mensaje);

        //Creando la instancia del servidor y configurando.
        Javalin app = Javalin.create(config ->{
            //configurando los documentos estaticos.
            config.staticFiles.add(staticFileConfig -> {
                staticFileConfig.hostedPath = "/";
                staticFileConfig.directory = "/publico";
                staticFileConfig.location = Location.CLASSPATH;
                staticFileConfig.precompress=false;
                staticFileConfig.aliasCheck=null;
            });

            //Habilitando el CORS. Ver: https://javalin.io/plugins/cors#getting-started para más opciones.
            config.plugins.enableCors(corsContainer -> {
                corsContainer.add(corsPluginConfig -> {
                    corsPluginConfig.anyHost();
                });
            });

            //habilitando el plugins de las rutas definidas.
            config.plugins.enableRouteOverview("/rutas");

            //Configurando el servicio SOAP en nuestro proyecto.
            //config.jetty.server(() -> new SoapControlador().agregarWebServicesSoap());

            //
            config.plugins.register(new OpenApiPlugin(
                    new OpenApiPluginConfiguration()
                            .withDocumentationPath("/openapi")
                            .withDefinitionConfiguration((version, definition) -> definition
                                            .withOpenApiInfo((openApiInfo) -> {
                                                openApiInfo.setTitle("Awesome App");
                                                openApiInfo.setVersion("1.0.0");
                                            })
                                            .withServer((openApiServer) -> {
                                                openApiServer.setUrl(("http://localhost:{port}{basePath}/" + version + "/"));
                                                openApiServer.setDescription("Server description goes here");
                                                openApiServer.addVariable("port", "8080", new String[] { "7070", "8080" }, "Port of the server");
                                                openApiServer.addVariable("basePath", "", new String[] { "", "v1" }, "Base path of the server");
                                            })

                            )
            ));

            SwaggerConfiguration swaggerConfiguration = new SwaggerConfiguration();
            //swaggerConfiguration.setDocumentationPath(deprecatedDocsPath);
            config.plugins.register(new SwaggerPlugin(swaggerConfiguration));

            ReDocConfiguration reDocConfiguration = new ReDocConfiguration();
            //reDocConfiguration.setDocumentationPath(deprecatedDocsPath);
            config.plugins.register(new ReDocPlugin(reDocConfiguration));

            for (JsonSchemaResource generatedJsonSchema : new JsonSchemaLoader().loadGeneratedSchemes()) {
                System.out.println(generatedJsonSchema.getName());
                System.out.println(generatedJsonSchema.getContentAsString());
            }


        });

        //

        //
        app.start(getHerokuAssignedPort());

        //creando el manejador
        app.get("/", ctx -> ctx.result("Hola Mundo en Javalin :-D"));

        //aplicando los diferentes conceptos.
        new CrudTradicionalControlador(app).aplicarRutas();


    }

    /**
     * Metodo para indicar el puerto en Heroku
     * @return
     */
    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 7000; //Retorna el puerto por defecto en caso de no estar en Heroku.
    }

}
