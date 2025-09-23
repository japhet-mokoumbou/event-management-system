package com.eventmanagement.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration}")
    private int jwtExpirationInMs;

    //génère la clé de signature (clé HMAC) à partir de la clé secrete
    private SecretKey getSigningKey(){
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    //génération du token
    public String generateToken(Authentication authentication){

        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
        Date expiryDate = new Date(System.currentTimeMillis() + jwtExpirationInMs);

        return Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();

    }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSigningKey()) // Définit la clé pour vérifier la signature
                .build() // Construit le parser
                .parseSignedClaims(token) //Parse et vérifie le JWT signé
                .getPayload(); //  Récupère le corps (les claims)

        return claims.getSubject(); // Retourne le "subject" (souvent le username)
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(authToken);  // remplace parseClaimsJws
            return true;
        } catch (SecurityException ex) {
            logger.error("Signature JWT invalide");
        } catch (MalformedJwtException ex) {
            logger.error("Token JWT invalide");
        } catch (ExpiredJwtException ex) {
            logger.error("Token JWT expiré");
        } catch (UnsupportedJwtException ex) {
            logger.error("Token JWT non supporté");
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string est vide");
        }
        return false;
    }

}
