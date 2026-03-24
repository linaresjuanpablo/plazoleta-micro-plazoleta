package com.gastropolis.plazoleta.domain.spi;

import com.gastropolis.plazoleta.domain.model.RestaurantModel;
import org.springframework.data.domain.Page;

public interface IRestaurantPersistencePort {
    RestaurantModel saveRestaurant(RestaurantModel restaurantModel);
    Page<RestaurantModel> findAllRestaurantsSortedByName(int page, int size);
    RestaurantModel findById(Long id);
    RestaurantModel findByOwnerId(Long ownerId);
}
