package com.example.agilizapp.api.auth.service.exception;


import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends AbstractAgilizappException {

    public ResourceNotFoundException(final String message) {
        super(message, HttpStatus.NOT_FOUND.value());
    }

}