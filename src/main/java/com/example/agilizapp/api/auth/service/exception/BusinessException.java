package com.example.agilizapp.api.auth.service.exception;

import org.springframework.http.HttpStatus;

public class BusinessException extends AbstractAgilizappException {

    public BusinessException(final String message) {
        super(message, HttpStatus.BAD_REQUEST.value());
    }

}