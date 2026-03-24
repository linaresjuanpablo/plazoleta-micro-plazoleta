package com.gastropolis.plazoleta.application.handler;

import com.gastropolis.plazoleta.application.dto.request.CreateEmployeeRequestDto;
import com.gastropolis.plazoleta.application.dto.request.CreateRestaurantRequestDto;
import com.gastropolis.plazoleta.application.dto.response.RestaurantResponseDto;
import org.springframework.data.domain.Page;

public interface IRestaurantHandler {
    RestaurantResponseDto createRestaurant(CreateRestaurantRequestDto dto, String token);
    Page<RestaurantResponseDto> listRestaurants(int page, int size);
    void createEmployee(CreateEmployeeRequestDto dto, String token);
}
