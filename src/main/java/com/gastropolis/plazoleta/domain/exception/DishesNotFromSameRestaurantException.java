package com.gastropolis.plazoleta.domain.exception;

public class DishesNotFromSameRestaurantException extends RuntimeException {
    public DishesNotFromSameRestaurantException(String message) {
        super(message);
    }
    public DishesNotFromSameRestaurantException() {
        super("Todos los platos deben pertenecer al mismo restaurante");
    }
}
