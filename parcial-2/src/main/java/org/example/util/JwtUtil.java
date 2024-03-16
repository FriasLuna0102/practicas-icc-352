package org.example.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.example.clases.Encuesta;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class JwtUtil {
    private static final String SECRETKEY = "F%$VKj!KJWW$%&Un3L7dSjuD3h2ks*4*rMb#2XkL@XrQxpfLx*Rf&^CFw9Vctam#%Wpa9%";

    public static String generarToken(){
        String token = "";
        SecretKey llave = Keys.hmacShaKeyFor(SECRETKEY.getBytes());

        LocalDateTime currentDate = LocalDateTime.now();
        LocalDateTime expirationDate = currentDate.plusMonths(1);
        Date exp = Date.from(expirationDate.atZone(ZoneId.systemDefault()).toInstant());


        token = Jwts.builder()
                .setIssuer("ClienteWeb")
                .setSubject("Auth JWT")
                .claim("usuario", Encuesta.getInstance().getUsuario())
                .setExpiration(exp)
                .signWith(llave, SignatureAlgorithm.HS256)
                .compact();

        return token;
    }

    public static boolean verificarToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(SECRETKEY.getBytes())
                    .build()
                    .parseClaimsJws(token);
            return true; // El token es válido
        } catch (Exception e) {
            return false; // El token no es válido
        }
    }
}
