package ru.medoedoed.common.response;

import org.springframework.http.HttpStatus;

import java.util.UUID;

public class InvalidTenderResponse extends BadResponse {
    public InvalidTenderResponse(UUID tenderId) {
        super("Tender not found: " + tenderId, HttpStatus.NOT_FOUND);
    }
}
