package com.gastropolis.plazoleta.infrastructure.out.jpa.adapter;

import com.gastropolis.plazoleta.domain.model.DishModel;
import com.gastropolis.plazoleta.domain.spi.IDishPersistencePort;
import com.gastropolis.plazoleta.infrastructure.out.jpa.entity.CategoryEntity;
import com.gastropolis.plazoleta.infrastructure.out.jpa.entity.DishEntity;
import com.gastropolis.plazoleta.infrastructure.out.jpa.entity.RestaurantEntity;
import com.gastropolis.plazoleta.infrastructure.out.jpa.mapper.IDishEntityMapper;
import com.gastropolis.plazoleta.infrastructure.out.jpa.repository.ICategoryRepository;
import com.gastropolis.plazoleta.infrastructure.out.jpa.repository.IDishRepository;
import com.gastropolis.plazoleta.infrastructure.out.jpa.repository.IRestaurantRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public class DishJpaAdapter implements IDishPersistencePort {

    private final IDishRepository dishRepository;
    private final IDishEntityMapper dishEntityMapper;
    private final ICategoryRepository categoryRepository;
    private final IRestaurantRepository restaurantRepository;

    public DishJpaAdapter(IDishRepository dishRepository, IDishEntityMapper dishEntityMapper,
                          ICategoryRepository categoryRepository, IRestaurantRepository restaurantRepository) {
        this.dishRepository = dishRepository;
        this.dishEntityMapper = dishEntityMapper;
        this.categoryRepository = categoryRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public DishModel saveDish(DishModel dishModel) {
        DishEntity entity;
        if (dishModel.getId() != null) {
            entity = dishRepository.findById(dishModel.getId()).orElse(new DishEntity());
        } else {
            entity = new DishEntity();
        }

        entity.setName(dishModel.getName());
        entity.setDescription(dishModel.getDescription());
        entity.setPrice(dishModel.getPrice());
        entity.setImageUrl(dishModel.getImageUrl());
        entity.setActive(dishModel.getActive());

        CategoryEntity category = categoryRepository.getReferenceById(dishModel.getCategoryId());
        entity.setCategory(category);

        RestaurantEntity restaurant = restaurantRepository.getReferenceById(dishModel.getRestaurantId());
        entity.setRestaurant(restaurant);

        DishEntity saved = dishRepository.save(entity);
        return dishEntityMapper.toModel(saved);
    }

    @Override
    public DishModel findById(Long id) {
        return dishRepository.findById(id)
                .map(dishEntityMapper::toModel)
                .orElse(null);
    }

    @Override
    public Page<DishModel> findByRestaurantId(Long restaurantId, int page, int size) {
        Page<DishEntity> entityPage = dishRepository.findByRestaurantId(restaurantId, PageRequest.of(page, size));
        return entityPage.map(dishEntityMapper::toModel);
    }

    @Override
    public Page<DishModel> findByRestaurantIdAndCategoryId(Long restaurantId, Long categoryId, int page, int size) {
        Page<DishEntity> entityPage = dishRepository.findByRestaurantIdAndCategoryId(restaurantId, categoryId, PageRequest.of(page, size));
        return entityPage.map(dishEntityMapper::toModel);
    }
}
