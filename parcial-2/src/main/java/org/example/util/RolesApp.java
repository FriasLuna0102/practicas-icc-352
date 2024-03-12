package org.example.util;

import io.javalin.security.RouteRole;

public enum RolesApp implements RouteRole {
    //CUALQUIERA,
    //LOGUEADO,
    ROLE_USUARIO,
    ROLE_ADMIN;
}
