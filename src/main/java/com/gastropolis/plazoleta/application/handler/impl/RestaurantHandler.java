package com.gastropolis.plazoleta.application.handler.impl;

import com.gastropolis.plazoleta.application.dto.request.CreateEmployeeRequestDto;
import com.gastropolis.plazoleta.application.dto.request.CreateRestaurantRequestDto;
import com.gastropolis.plazoleta.application.dto.response.RestaurantResponseDto;
import com.gastropolis.plazoleta.application.handler.IRestaurantHandler;
import com.gastropolis.plazoleta.application.mapper.IRestaurantRequestMapper;
import com.gastropolis.plazoleta.application.mapper.IRestaurantResponseMapper;
import com.gastropolis.plazoleta.domain.api.IRestaurantServicePort;
import com.gastropolis.plazoleta.domain.model.CreateEmployeeModel;
import com.gastropolis.plazoleta.domain.model.RestaurantModel;
import com.gastropolis.plazoleta.infrastructure.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class RestaurantHandler implements IRestaurantHandler {

    private final IRestaurantServicePort restaurantServicePort;
    private final IRestaurantRequestMapper restaurantRequestMapper;
    private final IRestaurantResponseMapper restaurantResponseMapper;
    private final JwtService jwtService;

    @Override
    public RestaurantResponseDto createRestaurant(CreateRestaurantRequestDto dto, String token) {
        RestaurantModel model = restaurantRequestMapper.toRestaurantModel(dto);
        RestaurantModel saved = restaurantServicePort.createRestaurant(model, token);
        return restaurantResponseMapper.toRestaurantResponseDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RestaurantResponseDto> listRestaurants(int page, int size) {
        Page<RestaurantModel> restaurantPage = restaurantServicePort.listRestaurants(page, size);
        return restaurantPage.map(restaurantResponseMapper::toRestaurantResponseDto);
    }

    @Override
    public void createEmployee(CreateEmployeeRequestDto dto, String token) {
        String rawToken = token.startsWith("Bearer ") ? token.substring(7) : token;
        Long ownerId = jwtService.getUserIdFromToken(rawToken);

        CreateEmployeeModel model = new CreateEmployeeModel(
                dto.getName(),
                dto.getLastName(),
                dto.getDni(),
                dto.getPhone(),
                dto.getBirthDate(),
                dto.getEmail(),
                dto.getPassword()
        );

        restaurantServicePort.createEmployee(model, ownerId, token);
    }
}
