package com.gastropolis.plazoleta.application.handler;

import com.gastropolis.plazoleta.application.dto.request.CreateDishRequestDto;
import com.gastropolis.plazoleta.application.dto.request.UpdateDishRequestDto;
import com.gastropolis.plazoleta.application.dto.response.DishResponseDto;
import org.springframework.data.domain.Page;

public interface IDishHandler {
    DishResponseDto createDish(CreateDishRequestDto dto, Long authenticatedOwnerId);
    DishResponseDto updateDish(Long dishId, UpdateDishRequestDto dto, Long authenticatedOwnerId);
    DishResponseDto toggleDishActive(Long dishId, Long authenticatedOwnerId);
    Page<DishResponseDto> listDishesByRestaurant(Long restaurantId, Long categoryId, int page, int size);
}
