package com.mycompany.jwtdemo.util;

import com.mycompany.jwtdemo.service.UserDetailsImpl;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JwtUtil {

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtExpirationMs}")
    private Integer jwtExpirationMs;

    //* Generate the JWT token
    public String generateJWTToken(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return Jwts.builder().setSubject(userDetails.getUsername()).setIssuedAt(new Date())
            .setExpiration(new Date(new Date().getTime() + jwtExpirationMs)).signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
    }

    //* Get username from valid JWT token
    public String getUsernameFromJwtToken(String token) {
        String username;
        try {
            username = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
            return username;
        } catch (Exception e) {
            log.error("Some error occurred while parsing JWT token");
            throw new RuntimeException(e);
        }
    }

    //* Validate the JWT token
    public boolean validateJwtToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
            return true;
        } catch (SignatureException e) {
            log.error("invalid Jwt signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("invalid Jwt token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("Jwt token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("Jwt token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("Jwt claims string is empty: {}", e.getMessage());
        }
        return false;
    }
}
