package ru.medoedoed.common.exceptions;

import lombok.Getter;

import java.util.UUID;

@Getter
public class InvalidOrganizationException extends RuntimeException {
    public InvalidOrganizationException(UUID organizationId) {
        super("Invalid username: " + organizationId);
        this.organizationId = organizationId;
    }

    private final UUID organizationId;
}
