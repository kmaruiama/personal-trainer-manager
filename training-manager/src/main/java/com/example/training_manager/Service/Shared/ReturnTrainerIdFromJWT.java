package com.example.training_manager.Service.Shared;

import com.example.training_manager.Exception.CustomException;
import com.example.training_manager.Security.SecurityConstant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;

public class ReturnTrainerIdFromJWT {
    public static long execute(String authHeader){
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new CustomException.InvalidHeader("Header da requisição é inválido");
        }

        String authHeaderChop = authHeader.substring(7);
        Claims claims;

        try {
            claims = Jwts.parserBuilder()
                    .setSigningKey(SecurityConstant.jwtPublicKey)
                    .build()
                    .parseClaimsJws(authHeaderChop)
                    .getBody();
        } catch (MalformedJwtException | SignatureException e) {
            throw new IllegalArgumentException("Token inválido: " + e.getMessage());
        }

        Object trainerIdObject = claims.get("trainerId");
        String trainerId = (trainerIdObject != null) ? trainerIdObject.toString().replace("Optional[", "").replace("]", "") : null;

        if (trainerId == null || trainerId.isEmpty()) {
            throw new IllegalArgumentException("Id não encontrado.");
        }

        return Long.parseLong(trainerId);
    }
}
