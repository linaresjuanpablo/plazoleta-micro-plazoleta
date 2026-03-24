package com.gastropolis.plazoleta.domain.api;

import com.gastropolis.plazoleta.domain.model.DishModel;
import org.springframework.data.domain.Page;

public interface IDishServicePort {
    DishModel createDish(DishModel dishModel, Long authenticatedOwnerId);
    DishModel updateDish(Long dishId, Integer price, String description, Long authenticatedOwnerId);
    DishModel toggleDishActive(Long dishId, Long authenticatedOwnerId);
    Page<DishModel> listDishesByRestaurant(Long restaurantId, Long categoryId, int page, int size);
    DishModel getDishById(Long id);
}
