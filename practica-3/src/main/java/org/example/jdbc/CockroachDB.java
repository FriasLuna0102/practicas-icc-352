package org.example.jdbc;

import org.postgresql.ds.PGSimpleDataSource;

import java.sql.*;

public class CockroachDB {
    private static PGSimpleDataSource ds;

    private static void inicializar() {
        ds = new PGSimpleDataSource();
        ds.setUrl(System.getenv("JDBC_DATABASE_URL"));
    }


    public static void crearTablaActividad() throws SQLException {
        if (ds == null) {
            inicializar();
        }
        Connection con = ds.getConnection();

        String query = "CREATE TABLE IF NOT EXISTS actividad (id SERIAL PRIMARY KEY, usuario VARCHAR(255), fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
        try (Statement statement = con.createStatement()) {
            statement.executeUpdate(query);
            System.out.println("Tabla 'actividad' creada correctamente");
        } finally {
            con.close();
        }
    }

    public static void insertarDataLogueo(String username) throws SQLException {
        if (ds == null) {
            inicializar();
        }
        Connection con = ds.getConnection();

        // Crear la tabla "actividad" si no existe
        crearTablaActividad();

        // Insertar datos en la tabla "actividad"
        StringBuilder sb = new StringBuilder("INSERT INTO actividad (usuario) VALUES (?)");
        String query = sb.toString();

        try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            preparedStatement.execute();
            // Registro de actividad de inserción
            System.out.println("Se ha insertado actividad para el usuario: " + username);
        } finally {
            con.close();
        }
    }

    public static void consultarActividad() throws SQLException {
        if (ds == null) {
            inicializar();
        }
        Connection con = ds.getConnection();

        String query = "SELECT * FROM actividadd";

        try (Statement statement = con.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                String usuario = resultSet.getString("usuario");
                Timestamp timestamp = resultSet.getTimestamp("fecha"); // Suponiendo que tengas una columna de fecha
                // Puedes imprimir o manejar la actividad como necesites
                System.out.println("Usuario: " + usuario + ", Fecha: " + timestamp);
            }
        } finally {
            con.close();
        }
    }
}
