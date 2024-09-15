package ru.medoedoed.common.response;

import org.springframework.http.HttpStatus;

import java.util.UUID;

public class InvalidOrganizationResponse extends BadResponse {
    public InvalidOrganizationResponse(UUID organizationId) {
        super("Organization not found: " + organizationId, HttpStatus.NOT_FOUND);
    }
}
