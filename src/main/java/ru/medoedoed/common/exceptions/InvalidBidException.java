package ru.medoedoed.common.exceptions;

import lombok.Getter;

import java.util.UUID;

@Getter
public class InvalidBidException extends RuntimeException {
    public InvalidBidException(UUID bidId) {
        super("Invalid bid: " + bidId);
        this.bidId = bidId;
    }

    private final UUID bidId;
}
