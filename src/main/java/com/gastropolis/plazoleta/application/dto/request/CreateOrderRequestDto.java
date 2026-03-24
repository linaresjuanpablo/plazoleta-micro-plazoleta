package com.gastropolis.plazoleta.application.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateOrderRequestDto {

    @NotNull(message = "El restaurante es obligatorio")
    private Long restaurantId;

    @NotEmpty(message = "El pedido debe tener al menos un plato")
    @Valid
    private List<OrderDishRequestDto> dishes;
}
