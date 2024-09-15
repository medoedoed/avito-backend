package ru.medoedoed.common.exceptions;

import lombok.Getter;

import java.util.UUID;

@Getter
public class InsufficientRightsException extends RuntimeException {
    public InsufficientRightsException(String username, UUID organizationId) {
        super("Insufficient rights for user " +
                username +
                " to access the organization with id "
                + organizationId);
        this.username = username;
        this.organizationId = organizationId;
    }

    private final String username;
    private final UUID organizationId;
}
