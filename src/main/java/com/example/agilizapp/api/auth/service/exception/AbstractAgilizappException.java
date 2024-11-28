package com.example.agilizapp.api.auth.service.exception;

public abstract class AbstractAgilizappException extends RuntimeException {

    private final Integer code;

    public AbstractAgilizappException(final String message, final Integer code) {
        super(message);
        this.code = code;
    }

    public AbstractAgilizappException(final String message, final Integer code, final Throwable throwable) {
        super(message, throwable);
        this.code = code;
    }

}