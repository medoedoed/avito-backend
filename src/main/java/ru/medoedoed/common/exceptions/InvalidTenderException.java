package ru.medoedoed.common.exceptions;

import lombok.Getter;

import java.util.UUID;

@Getter
public class InvalidTenderException extends RuntimeException {
    public InvalidTenderException(UUID tenderId) {
        super("Invalid tender: " + tenderId);
        this.tenderId = tenderId;
    }

    private final UUID tenderId;
}
