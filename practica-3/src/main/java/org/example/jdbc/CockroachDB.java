package org.example.jdbc;

import org.postgresql.ds.PGSimpleDataSource;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CockroachDB {
	private static PGSimpleDataSource ds;

	private static void inicializar() {
		ds = new PGSimpleDataSource();
		ds.setUrl(System.getenv("JDBC_DATABASE_URL"));
		ds.setUser("randae");
		ds.setPassword("In8zhS6I3sRtuWAmE-R80A");
	}

	public static void insertarDataLogueo(String username) {
		if (ds == null){
			inicializar();
		}

		StringBuilder sb = new StringBuilder("INSERT INTO ").append("actividad")
						.append("(usuario)")
						.append("VALUES (?)");

		String query = sb.toString();
		PreparedStatement preparedStatement =
	}
}
