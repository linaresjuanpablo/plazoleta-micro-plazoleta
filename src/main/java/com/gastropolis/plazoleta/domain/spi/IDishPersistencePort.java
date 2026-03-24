package com.gastropolis.plazoleta.domain.spi;

import com.gastropolis.plazoleta.domain.model.DishModel;
import org.springframework.data.domain.Page;

public interface IDishPersistencePort {
    DishModel saveDish(DishModel dishModel);
    DishModel findById(Long id);
    Page<DishModel> findByRestaurantId(Long restaurantId, int page, int size);
    Page<DishModel> findByRestaurantIdAndCategoryId(Long restaurantId, Long categoryId, int page, int size);
}
