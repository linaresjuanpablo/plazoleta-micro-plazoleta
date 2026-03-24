package com.gastropolis.plazoleta.domain.usecase;

import com.gastropolis.plazoleta.domain.api.IDishServicePort;
import com.gastropolis.plazoleta.domain.exception.DishNotFoundException;
import com.gastropolis.plazoleta.domain.exception.NotRestaurantOwnerException;
import com.gastropolis.plazoleta.domain.exception.RestaurantNotFoundException;
import com.gastropolis.plazoleta.domain.model.DishModel;
import com.gastropolis.plazoleta.domain.model.RestaurantModel;
import com.gastropolis.plazoleta.domain.spi.IDishPersistencePort;
import com.gastropolis.plazoleta.domain.spi.IRestaurantPersistencePort;
import org.springframework.data.domain.Page;

public class DishUseCase implements IDishServicePort {

    private final IDishPersistencePort dishPersistencePort;
    private final IRestaurantPersistencePort restaurantPersistencePort;

    public DishUseCase(IDishPersistencePort dishPersistencePort, IRestaurantPersistencePort restaurantPersistencePort) {
        this.dishPersistencePort = dishPersistencePort;
        this.restaurantPersistencePort = restaurantPersistencePort;
    }

    @Override
    public DishModel createDish(DishModel dishModel, Long authenticatedOwnerId) {
        RestaurantModel restaurant = restaurantPersistencePort.findById(dishModel.getRestaurantId());
        if (restaurant == null) {
            throw new RestaurantNotFoundException();
        }
        if (!restaurant.getOwnerId().equals(authenticatedOwnerId)) {
            throw new NotRestaurantOwnerException();
        }
        dishModel.setActive(true);
        return dishPersistencePort.saveDish(dishModel);
    }

    @Override
    public DishModel updateDish(Long dishId, Integer price, String description, Long authenticatedOwnerId) {
        DishModel dish = dishPersistencePort.findById(dishId);
        if (dish == null) {
            throw new DishNotFoundException();
        }
        RestaurantModel restaurant = restaurantPersistencePort.findById(dish.getRestaurantId());
        if (restaurant == null) {
            throw new RestaurantNotFoundException();
        }
        if (!restaurant.getOwnerId().equals(authenticatedOwnerId)) {
            throw new NotRestaurantOwnerException();
        }
        if (price != null) {
            dish.setPrice(price);
        }
        if (description != null) {
            dish.setDescription(description);
        }
        return dishPersistencePort.saveDish(dish);
    }

    @Override
    public DishModel toggleDishActive(Long dishId, Long authenticatedOwnerId) {
        DishModel dish = dishPersistencePort.findById(dishId);
        if (dish == null) {
            throw new DishNotFoundException();
        }
        RestaurantModel restaurant = restaurantPersistencePort.findById(dish.getRestaurantId());
        if (restaurant == null) {
            throw new RestaurantNotFoundException();
        }
        if (!restaurant.getOwnerId().equals(authenticatedOwnerId)) {
            throw new NotRestaurantOwnerException();
        }
        dish.setActive(!dish.getActive());
        return dishPersistencePort.saveDish(dish);
    }

    @Override
    public Page<DishModel> listDishesByRestaurant(Long restaurantId, Long categoryId, int page, int size) {
        if (categoryId != null) {
            return dishPersistencePort.findByRestaurantIdAndCategoryId(restaurantId, categoryId, page, size);
        }
        return dishPersistencePort.findByRestaurantId(restaurantId, page, size);
    }

    @Override
    public DishModel getDishById(Long id) {
        DishModel dish = dishPersistencePort.findById(id);
        if (dish == null) {
            throw new DishNotFoundException();
        }
        return dish;
    }
}
