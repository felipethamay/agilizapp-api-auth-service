package com.example.agilizapp.api.auth.service.exception;


import org.springframework.http.HttpStatus;

public class UnauthorizedException extends AbstractAgilizappException {

    public UnauthorizedException(final String message) {
        super(message, HttpStatus.UNAUTHORIZED.value());
    }

}