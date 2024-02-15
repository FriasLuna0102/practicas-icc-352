package org.example.jdbc;

import org.postgresql.ds.PGSimpleDataSource;

public class CockroachDB {

	public static void main(String[] args) {
		PGSimpleDataSource ds = new PGSimpleDataSource();
		ds.setUrl(System.getenv("JDBC_DATABASE_URL"));
		ds.setUser("randae");
		ds.setPassword("In8zhS6I3sRtuWAmE-R80A");

	}
}
