package org.miage.banque.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.miage.banque.entities.Role;
import org.miage.banque.entities.client.Client;
import org.miage.banque.exceptions.InvalidTokenException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
public class Token {

    private static final Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());

    public static Map<String, String> generateTokens(Client client, HttpServletRequest request) {
        log.info("Generation des tokens pour le client {}", client.getEmail());

        String accessToken = JWT.create()
                .withSubject(client.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis()  + 10 * 60 * 1000)) //Valid for 10 minutes.
                .withIssuer(request.getRequestURL().toString())
                .withClaim("roles", client.getRoles().stream().map(Role::getNom).collect(Collectors.toList()))
                .sign(algorithm);

        String refreshToken = JWT.create()
                .withSubject(client.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis()  + 1440 * 60 * 1000)) //Valid for 1 day.
                .withIssuer(request.getRequestURL().toString())
                .sign(algorithm);

        Map<String,String> tokens = new HashMap<>();
        tokens.put("access_token", accessToken);
        tokens.put("refresh_token", refreshToken);

        return tokens;
    }

    public static String extractUser(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String refreshToken = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refreshToken);
                return decodedJWT.getSubject();
            }
            catch (Exception e) {
                throw new InvalidTokenException("Token invalide");
            }
        }else{
            throw new InvalidTokenException("Vous avez besoin d'un token pour accéder à cette ressource");
        }
    }
}
