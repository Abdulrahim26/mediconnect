package com.mediconnect.mediconnectapi.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


@Service
public class JwtService {


    @Value("${jwt.secret}")
    private String secret;


    @Value("${jwt.expiration}")
    private long jwtExpiration;


    private SecretKey getSigningKey() {

        return Keys.hmacShaKeyFor(
                secret.getBytes()
        );
    }


    public String generateToken(String email, String role) {


        Map<String, Object> claims = new HashMap<>();

        claims.put("role", role);


        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(
                        new Date(
                                System.currentTimeMillis()
                                        + jwtExpiration
                        )
                )
                .signWith(
                        getSigningKey(),
                        SignatureAlgorithm.HS256
                )
                .compact();
    }


    public String extractEmail(String token) {

        return extractClaim(
                token,
                Claims::getSubject
        );
    }


    public <T> T extractClaim(
            String token,
            Function<Claims, T> resolver
    ) {

        final Claims claims =
                Jwts.parserBuilder()
                        .setSigningKey(getSigningKey())
                        .build()
                        .parseClaimsJws(token)
                        .getBody();


        return resolver.apply(claims);
    }


    public boolean isTokenValid(String token) {

        try {

            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);


            return true;

        } catch (Exception e) {

            return false;
        }
    }
}