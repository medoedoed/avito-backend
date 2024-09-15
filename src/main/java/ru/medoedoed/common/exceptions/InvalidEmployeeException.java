package ru.medoedoed.common.exceptions;

import lombok.Getter;

@Getter
public class InvalidEmployeeException extends RuntimeException {
    public InvalidEmployeeException(String username) {
        super("Invalid username: " + username);
        this.username = username;
    }

    private final String username;
}
