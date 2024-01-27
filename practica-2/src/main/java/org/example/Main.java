package org.example;

import  org.example.clases.Articulo;
import  org.example.clases.Comentario;
import  org.example.clases.Usuario;
import  org.example.clases.Etiqueta;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");

        Etiqueta prueba = new Etiqueta(1014,"SuperHeroes");

        System.out.println(prueba.getId());

        prueba.setId(256);

        System.out.println(prueba.getId());

    }
}