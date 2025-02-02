package com.example.training_manager.Security;

import io.jsonwebtoken.*;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.stream.Collectors;

@Component
public class TokenGenerator {
    public String generator(String trainerId, Authentication authentication) {
        String username = authentication.getName();
        Date currentDate = new Date();
        Date expirationDate = new Date(currentDate.getTime() + SecurityConstant.jwtExpirationDate);
        String roles = authentication
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        String token = Jwts.builder()
                .setSubject(username)
                .claim("trainerId", trainerId)
                .claim("roles", roles)
                .setIssuedAt(currentDate)
                .setExpiration(expirationDate)
                .signWith(SecurityConstant.jwtPrivateKey, SignatureAlgorithm.ES256)
                .compact();
        return token;
    }

    public String getUsernameFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SecurityConstant.jwtPublicKey)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public String getRolesFromJWT(String token){
        Claims claims = Jwts.parser()
                .setSigningKey(SecurityConstant.jwtPublicKey)
                .parseClaimsJws(token)
                .getBody();
        return claims.get("roles", String.class);
    }


    public boolean validateJWT(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(SecurityConstant.jwtPublicKey)
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            throw new AuthenticationCredentialsNotFoundException("Sua sessão expirou.");
        } catch (SignatureException e) {
            throw new AuthenticationCredentialsNotFoundException("Token inválido.");
        } catch (JwtException e) {
            throw new AuthenticationCredentialsNotFoundException("Erro ao validar o token.");
        } catch (Exception e) {
            throw new AuthenticationCredentialsNotFoundException("Erro inesperado.");
        }
    }

}