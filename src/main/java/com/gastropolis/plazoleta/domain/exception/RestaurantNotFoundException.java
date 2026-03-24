package com.gastropolis.plazoleta.domain.exception;

public class RestaurantNotFoundException extends RuntimeException {
    public RestaurantNotFoundException(String message) {
        super(message);
    }
    public RestaurantNotFoundException() {
        super("Restaurante no encontrado");
    }
}
