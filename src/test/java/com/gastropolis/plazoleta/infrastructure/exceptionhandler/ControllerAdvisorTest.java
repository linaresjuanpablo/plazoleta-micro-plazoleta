package com.gastropolis.plazoleta.infrastructure.exceptionhandler;

import com.gastropolis.plazoleta.domain.exception.*;
import feign.FeignException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ControllerAdvisorTest {

    private ControllerAdvisor controllerAdvisor;

    @BeforeEach
    void setUp() {
        controllerAdvisor = new ControllerAdvisor();
    }

    @Test
    void handleClientHasActiveOrder_returnsConflict() {
        ClientHasActiveOrderException ex = new ClientHasActiveOrderException("El cliente ya tiene un pedido activo");

        ResponseEntity<ExceptionResponse> response = controllerAdvisor.handleClientHasActiveOrder(ex);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("El cliente ya tiene un pedido activo", response.getBody().getMessage());
    }

    @Test
    void handleInvalidOrderState_returnsConflict() {
        InvalidOrderStateException ex = new InvalidOrderStateException("Estado de pedido inválido");

        ResponseEntity<ExceptionResponse> response = controllerAdvisor.handleInvalidOrderState(ex);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Estado de pedido inválido", response.getBody().getMessage());
    }

    @Test
    void handleInvalidPin_returnsBadRequest() {
        InvalidPinException ex = new InvalidPinException("PIN inválido");

        ResponseEntity<ExceptionResponse> response = controllerAdvisor.handleInvalidPin(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("PIN inválido", response.getBody().getMessage());
    }

    @Test
    void handleNotRestaurantOwner_returnsForbidden() {
        NotRestaurantOwnerException ex = new NotRestaurantOwnerException("No es propietario del restaurante");

        ResponseEntity<ExceptionResponse> response = controllerAdvisor.handleNotRestaurantOwner(ex);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("No es propietario del restaurante", response.getBody().getMessage());
    }

    @Test
    void handleRestaurantNotFound_returnsNotFound() {
        RestaurantNotFoundException ex = new RestaurantNotFoundException("Restaurante no encontrado");

        ResponseEntity<ExceptionResponse> response = controllerAdvisor.handleRestaurantNotFound(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Restaurante no encontrado", response.getBody().getMessage());
    }

    @Test
    void handleDishNotFound_returnsNotFound() {
        DishNotFoundException ex = new DishNotFoundException("Plato no encontrado");

        ResponseEntity<ExceptionResponse> response = controllerAdvisor.handleDishNotFound(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Plato no encontrado", response.getBody().getMessage());
    }

    @Test
    void handleOrderNotFound_returnsNotFound() {
        OrderNotFoundException ex = new OrderNotFoundException("Pedido no encontrado");

        ResponseEntity<ExceptionResponse> response = controllerAdvisor.handleOrderNotFound(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Pedido no encontrado", response.getBody().getMessage());
    }

    @Test
    void handleDishesNotFromSameRestaurant_returnsBadRequest() {
        DishesNotFromSameRestaurantException ex = new DishesNotFromSameRestaurantException("Platos no son del mismo restaurante");

        ResponseEntity<ExceptionResponse> response = controllerAdvisor.handleDishesNotFromSameRestaurant(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Platos no son del mismo restaurante", response.getBody().getMessage());
    }

    @Test
    void handleIllegalArgument_returnsBadRequest() {
        IllegalArgumentException ex = new IllegalArgumentException("Argumento inválido");

        ResponseEntity<ExceptionResponse> response = controllerAdvisor.handleIllegalArgument(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Argumento inválido", response.getBody().getMessage());
    }

    @Test
    void handleValidationErrors_returnsBadRequestWithFieldErrors() {
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("dto", "name", "El nombre es obligatorio");

        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(List.of(fieldError));

        ResponseEntity<Map<String, String>> response = controllerAdvisor.handleValidationErrors(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("El nombre es obligatorio", response.getBody().get("name"));
    }

    @Test
    void handleFeignException_returnsBadGateway() {
        FeignException ex = mock(FeignException.class);
        when(ex.getMessage()).thenReturn("Connection refused");

        ResponseEntity<ExceptionResponse> response = controllerAdvisor.handleFeignException(ex);

        assertEquals(HttpStatus.BAD_GATEWAY, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getMessage().contains("Connection refused"));
    }
}
