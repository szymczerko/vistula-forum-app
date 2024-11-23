package com.vistulaforum.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.sql.Timestamp;
import java.util.Date;

@Component
public class JwtUtility {

    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    public String generateJsonWebToken(Authentication authentication) {
        String login = authentication.getName();
        Date currDate = new Date();
        Date expireDate = new Date(currDate.getTime() + 86400000);
        return Jwts.builder()
                .setSubject(login)
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public String getLoginFromJsonWebToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public Timestamp getExpirationDateFromJsonWebToken(String token) {
        DecodedJWT decodedJWT = JWT.decode(token);
        Date expirationDate = decodedJWT.getExpiresAt();
        return new Timestamp(expirationDate.getTime());
    }

    public boolean validateJsonWebToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

}