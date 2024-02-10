package org.example.util;

import io.javalin.Javalin;

public abstract class ControladorClass {

    protected Javalin app;

    public ControladorClass (Javalin app){
        this.app = app;
    }

    abstract public void aplicarRutas();

}
