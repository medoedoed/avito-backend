package ru.medoedoed.common.response;

import org.springframework.http.HttpStatus;

import java.util.UUID;

public class InvalidUserResponse extends BadResponse {
    public InvalidUserResponse(String username) {
        super("User not found or invalid: " + username, HttpStatus.UNAUTHORIZED);
    }

    public InvalidUserResponse(UUID userId) {
        super("User not found or invalid: " + userId, HttpStatus.UNAUTHORIZED);
    }
}
