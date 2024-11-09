package com.example.userservice.security.jwt;

import com.example.userservice.security.AppUserDetails;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;

@Component
@Slf4j
public class JwtUtils {

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.tokenExpiration}")
    private Duration tokenExpiration;

    public String generateJwtToken(AppUserDetails userDetails){
        return generateTokenFromUsername(userDetails.getUsername());
    }

    public String generateTokenFromUsername(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + tokenExpiration.toMillis()))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String getUsername(String token){
        return  Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validate(String token){
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        }catch (SignatureException ex){
            log.error("Invalid signature: {}", ex.getMessage());
        }catch (MalformedJwtException ex){
            log.error("Invalid token: {}", ex.getMessage());
        }catch (ExpiredJwtException ex){
            log.error("Token is expired: {}", ex.getMessage());
        }catch (UnsupportedJwtException ex){
            log.error("Token is unsupported: {}", ex.getMessage());
        }catch (IllegalArgumentException ex){
            log.error("Claims string id empty: {}", ex.getMessage());
        }
        return false;
    }
}
