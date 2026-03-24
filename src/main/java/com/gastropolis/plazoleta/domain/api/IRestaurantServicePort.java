package com.gastropolis.plazoleta.domain.api;

import com.gastropolis.plazoleta.domain.model.CreateEmployeeModel;
import com.gastropolis.plazoleta.domain.model.RestaurantModel;
import org.springframework.data.domain.Page;

public interface IRestaurantServicePort {
    RestaurantModel createRestaurant(RestaurantModel restaurantModel, String token);
    Page<RestaurantModel> listRestaurants(int page, int size);
    RestaurantModel getRestaurantById(Long id);
    void createEmployee(CreateEmployeeModel model, Long ownerId, String token);
}
