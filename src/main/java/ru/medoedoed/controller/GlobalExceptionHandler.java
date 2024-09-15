package ru.medoedoed.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.medoedoed.common.exceptions.InsufficientRightsException;
import ru.medoedoed.common.exceptions.InvalidBidException;
import ru.medoedoed.common.exceptions.InvalidEmployeeException;
import ru.medoedoed.common.exceptions.InvalidOrganizationException;
import ru.medoedoed.common.response.*;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(InvalidEmployeeException.class)
    public ResponseEntity<?> handleInvalidEmployeeException(InvalidEmployeeException ex) {
        return new InvalidUserResponse(ex.getUsername()).returnResponse();
    }

    @ExceptionHandler(InvalidBidException.class)
    public ResponseEntity<?> handleInvalidBidException(InvalidBidException ex) {
        return new InvalidBidResponse(ex.getBidId()).returnResponse();
    }

    @ExceptionHandler(InsufficientRightsException.class)
    public ResponseEntity<?> handleInsufficientRightsException(InsufficientRightsException ex) {
        return new InsufficientRightsResponse(ex.getUsername(), ex.getOrganizationId()).returnResponse();
    }
    @ExceptionHandler(InvalidOrganizationException.class)
    public ResponseEntity<?> handleInvalidOrganizationException(InvalidOrganizationException ex) {
        return new InvalidOrganizationResponse(ex.getOrganizationId()).returnResponse();
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        return new BadResponse("Invalid request body" , HttpStatus.BAD_REQUEST).returnResponse();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception ex) {
        var response = new InternalErrorResponse();
        response.setReason(ex.getMessage()); // если забуду убрать, то лежало это тут для дебага
        return response.returnResponse();

    }
}