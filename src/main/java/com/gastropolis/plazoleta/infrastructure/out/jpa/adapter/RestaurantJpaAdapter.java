package com.gastropolis.plazoleta.infrastructure.out.jpa.adapter;

import com.gastropolis.plazoleta.domain.model.RestaurantModel;
import com.gastropolis.plazoleta.domain.spi.IRestaurantPersistencePort;
import com.gastropolis.plazoleta.infrastructure.out.jpa.entity.RestaurantEntity;
import com.gastropolis.plazoleta.infrastructure.out.jpa.mapper.IRestaurantEntityMapper;
import com.gastropolis.plazoleta.infrastructure.out.jpa.repository.IRestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public class RestaurantJpaAdapter implements IRestaurantPersistencePort {

    private final IRestaurantRepository restaurantRepository;
    private final IRestaurantEntityMapper restaurantEntityMapper;

    public RestaurantJpaAdapter(IRestaurantRepository restaurantRepository, IRestaurantEntityMapper restaurantEntityMapper) {
        this.restaurantRepository = restaurantRepository;
        this.restaurantEntityMapper = restaurantEntityMapper;
    }

    @Override
    public RestaurantModel saveRestaurant(RestaurantModel restaurantModel) {
        RestaurantEntity entity = restaurantEntityMapper.toEntity(restaurantModel);
        RestaurantEntity saved = restaurantRepository.save(entity);
        return restaurantEntityMapper.toModel(saved);
    }

    @Override
    public Page<RestaurantModel> findAllRestaurantsSortedByName(int page, int size) {
        Page<RestaurantEntity> entityPage = restaurantRepository.findAllByOrderByNameAsc(PageRequest.of(page, size));
        return entityPage.map(restaurantEntityMapper::toModel);
    }

    @Override
    public RestaurantModel findById(Long id) {
        return restaurantRepository.findById(id)
                .map(restaurantEntityMapper::toModel)
                .orElse(null);
    }

    @Override
    public RestaurantModel findByOwnerId(Long ownerId) {
        return restaurantRepository.findByOwnerId(ownerId)
                .map(restaurantEntityMapper::toModel)
                .orElse(null);
    }
}
