package com.gastropolis.plazoleta.domain.exception;

public class InvalidPinException extends RuntimeException {
    public InvalidPinException(String message) {
        super(message);
    }
    public InvalidPinException() {
        super("El PIN de seguridad es inválido");
    }
}
