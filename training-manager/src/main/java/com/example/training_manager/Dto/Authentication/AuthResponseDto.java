package com.example.training_manager.Dto.Authentication;
import lombok.Data;

@Data
public class AuthResponseDto {
    private String tokenOwner;
    private String accessToken;
    private String tokenType = "Bearer ";

    public AuthResponseDto(String accessToken, String tokenOwner){
        this.accessToken = accessToken;
        this.tokenOwner = tokenOwner;
    }
}
