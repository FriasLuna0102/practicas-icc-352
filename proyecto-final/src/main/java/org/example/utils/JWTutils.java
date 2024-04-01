package org.example.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.io.UnsupportedEncodingException;
import java.util.Date;

public class JWTutils {

	private static final String SECRET_KEY = "ccoK4iHM^qBHpwwvG^";
	private static final long EXPIRATION_TIME = 86400000; // Tiempo de expiración del token (en milisegundos, aquí es un día)

	public static String generateJwt(String username) throws UnsupportedEncodingException {
		Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY); // Reemplaza "your-secret" con tu secreto
		String token = JWT.create()
				.withIssuer("icc352")
				.withSubject("proyecto")
				.withClaim("username", username)
				.sign(algorithm);
		return token;
	}

	public static DecodedJWT decodeJWT(String jwt) throws UnsupportedEncodingException {
		Algorithm algorithm = Algorithm.HMAC256("your-secret"); // Reemplaza "your-secret" con tu secreto
		DecodedJWT decodedJWT = JWT.require(algorithm)
				.withIssuer("auth0")
				.build()
				.verify(jwt);
		return decodedJWT;
	}
}
