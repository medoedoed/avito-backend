package ru.medoedoed.common.response;

import org.springframework.http.HttpStatus;

public class InternalErrorResponse extends BadResponse {
    public InternalErrorResponse() {
        super("Internal error", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
