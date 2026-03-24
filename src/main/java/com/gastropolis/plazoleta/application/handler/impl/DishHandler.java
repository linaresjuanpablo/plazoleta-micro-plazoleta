package com.gastropolis.plazoleta.application.handler.impl;

import com.gastropolis.plazoleta.application.dto.request.CreateDishRequestDto;
import com.gastropolis.plazoleta.application.dto.request.UpdateDishRequestDto;
import com.gastropolis.plazoleta.application.dto.response.DishResponseDto;
import com.gastropolis.plazoleta.application.handler.IDishHandler;
import com.gastropolis.plazoleta.application.mapper.IDishRequestMapper;
import com.gastropolis.plazoleta.application.mapper.IDishResponseMapper;
import com.gastropolis.plazoleta.domain.api.IDishServicePort;
import com.gastropolis.plazoleta.domain.model.DishModel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DishHandler implements IDishHandler {

    private final IDishServicePort dishServicePort;
    private final IDishRequestMapper dishRequestMapper;
    private final IDishResponseMapper dishResponseMapper;

    @Override
    public DishResponseDto createDish(CreateDishRequestDto dto, Long authenticatedOwnerId) {
        DishModel model = dishRequestMapper.toDishModel(dto);
        DishModel saved = dishServicePort.createDish(model, authenticatedOwnerId);
        return dishResponseMapper.toDishResponseDto(saved);
    }

    @Override
    public DishResponseDto updateDish(Long dishId, UpdateDishRequestDto dto, Long authenticatedOwnerId) {
        DishModel updated = dishServicePort.updateDish(dishId, dto.getPrice(), dto.getDescription(), authenticatedOwnerId);
        return dishResponseMapper.toDishResponseDto(updated);
    }

    @Override
    public DishResponseDto toggleDishActive(Long dishId, Long authenticatedOwnerId) {
        DishModel updated = dishServicePort.toggleDishActive(dishId, authenticatedOwnerId);
        return dishResponseMapper.toDishResponseDto(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DishResponseDto> listDishesByRestaurant(Long restaurantId, Long categoryId, int page, int size) {
        Page<DishModel> dishPage = dishServicePort.listDishesByRestaurant(restaurantId, categoryId, page, size);
        return dishPage.map(dishResponseMapper::toDishResponseDto);
    }
}
