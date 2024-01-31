package org.example.controladores;


import java.util.UUID;

public class GenerarId {

    private static int contador = 0;
    static private long id;
    // otros campos y métodos de la clase
    public GenerarId() {
        id = ++contador;
    }
     public static long getId() {
        return id;
    }

}


