package com.synclab.triphippie.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Base64;
import java.util.Date;


@Service
public class JwtUtil {

    private final Key key;

    public JwtUtil(){
        String secret = System.getProperty("JWT_SECRET_KEY");
        if (secret == null) {
            Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
            String encodedKey = Base64.getEncoder().encodeToString(key.getEncoded());
            System.err.println(encodedKey);
            throw new IllegalStateException("JWT_SECRET_KEY is not set");
        }
        this.key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret));
    }

    public String generateToken(String subject, Long id) {
        return Jwts.builder()
                .setSubject(subject)
                .claim("id", id)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 36000000)) // 1 hour expiration
                .signWith(key)
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Long extractId(String token) {
        Jws<Claims> claimsJws = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
        return claimsJws.getBody().get("id", Long.class);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, java.util.function.Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
    }

    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }
}
