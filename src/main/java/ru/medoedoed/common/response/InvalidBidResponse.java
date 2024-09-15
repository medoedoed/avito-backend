package ru.medoedoed.common.response;

import org.springframework.http.HttpStatus;

import java.util.UUID;

public class InvalidBidResponse extends BadResponse {
    public InvalidBidResponse(UUID bidId) {
        super("Bid not found: " + bidId, HttpStatus.NOT_FOUND);
    }
}
