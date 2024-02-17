package org.example.jdbc;

import org.postgresql.ds.PGSimpleDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CockroachDB {
	private static PGSimpleDataSource ds;

	private static void inicializar() {
		ds = new PGSimpleDataSource();
		ds.setUrl(System.getenv("JDBC_DATABASE_URL"));
	}

	public static void insertarDataLogueo(String username) throws SQLException {
		if (ds == null){
			inicializar();
		}
		Connection con = ds.getConnection();

		StringBuilder sb = new StringBuilder("INSERT INTO ").append("actividad")
						.append("(usuario)")
						.append("VALUES (?)");

		String query = sb.toString();
		PreparedStatement preparedStatement = con.prepareStatement(query);
		preparedStatement.setString(1, username);
		preparedStatement.execute();

		preparedStatement.close();
		con.close();
	}
}
