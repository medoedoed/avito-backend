package ru.medoedoed.common.response;

import org.springframework.http.HttpStatus;

import java.util.UUID;

public class InsufficientRightsResponse extends BadResponse {
    public InsufficientRightsResponse(String username, UUID organizationId) {
        super("Insufficient rights for user " +
                username +
                " to access the organization with id "
                + organizationId,
                HttpStatus.FORBIDDEN);
    }
}
