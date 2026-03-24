package com.gastropolis.plazoleta.infrastructure.out.jpa.adapter;

import com.gastropolis.plazoleta.domain.model.DishModel;
import com.gastropolis.plazoleta.infrastructure.out.jpa.entity.CategoryEntity;
import com.gastropolis.plazoleta.infrastructure.out.jpa.entity.DishEntity;
import com.gastropolis.plazoleta.infrastructure.out.jpa.entity.RestaurantEntity;
import com.gastropolis.plazoleta.infrastructure.out.jpa.mapper.IDishEntityMapper;
import com.gastropolis.plazoleta.infrastructure.out.jpa.repository.ICategoryRepository;
import com.gastropolis.plazoleta.infrastructure.out.jpa.repository.IDishRepository;
import com.gastropolis.plazoleta.infrastructure.out.jpa.repository.IRestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DishJpaAdapterTest {

    @Mock
    private IDishRepository dishRepository;

    @Mock
    private IDishEntityMapper dishEntityMapper;

    @Mock
    private ICategoryRepository categoryRepository;

    @Mock
    private IRestaurantRepository restaurantRepository;

    private DishJpaAdapter dishJpaAdapter;

    private DishModel dishModel;
    private DishEntity dishEntity;

    @BeforeEach
    void setUp() {
        dishJpaAdapter = new DishJpaAdapter(dishRepository, dishEntityMapper, categoryRepository, restaurantRepository);

        dishModel = new DishModel(null, "Hamburguesa", "Desc", 15000, "http://img.png", true, 2L, 1L);
        dishEntity = new DishEntity();
        dishEntity.setId(1L);
        dishEntity.setName("Hamburguesa");
    }

    @Test
    void saveDish_whenIdIsNull_createsNewEntityAndSaves() {
        CategoryEntity categoryEntity = new CategoryEntity();
        RestaurantEntity restaurantEntity = new RestaurantEntity();

        when(categoryRepository.getReferenceById(2L)).thenReturn(categoryEntity);
        when(restaurantRepository.getReferenceById(1L)).thenReturn(restaurantEntity);
        when(dishRepository.save(any(DishEntity.class))).thenReturn(dishEntity);
        when(dishEntityMapper.toModel(dishEntity)).thenReturn(dishModel);

        DishModel result = dishJpaAdapter.saveDish(dishModel);

        assertNotNull(result);
        verify(dishRepository, never()).findById(any());
        verify(dishRepository).save(any(DishEntity.class));
        verify(dishEntityMapper).toModel(dishEntity);
    }

    @Test
    void saveDish_whenIdIsNotNull_loadsExistingEntityAndUpdates() {
        DishModel modelWithId = new DishModel(10L, "Hamburguesa", "Nueva desc", 20000, "http://img2.png", false, 2L, 1L);
        DishEntity existingEntity = new DishEntity();
        existingEntity.setId(10L);

        CategoryEntity categoryEntity = new CategoryEntity();
        RestaurantEntity restaurantEntity = new RestaurantEntity();

        when(dishRepository.findById(10L)).thenReturn(Optional.of(existingEntity));
        when(categoryRepository.getReferenceById(2L)).thenReturn(categoryEntity);
        when(restaurantRepository.getReferenceById(1L)).thenReturn(restaurantEntity);
        when(dishRepository.save(existingEntity)).thenReturn(existingEntity);
        when(dishEntityMapper.toModel(existingEntity)).thenReturn(modelWithId);

        DishModel result = dishJpaAdapter.saveDish(modelWithId);

        assertNotNull(result);
        assertEquals(10L, result.getId());
        verify(dishRepository).findById(10L);
        verify(dishRepository).save(existingEntity);
    }

    @Test
    void saveDish_whenIdNotNullAndNotFound_createsNewEntity() {
        DishModel modelWithId = new DishModel(99L, "Hamburguesa", "Desc", 15000, "http://img.png", true, 2L, 1L);

        when(dishRepository.findById(99L)).thenReturn(Optional.empty());
        when(categoryRepository.getReferenceById(2L)).thenReturn(new CategoryEntity());
        when(restaurantRepository.getReferenceById(1L)).thenReturn(new RestaurantEntity());
        when(dishRepository.save(any(DishEntity.class))).thenReturn(dishEntity);
        when(dishEntityMapper.toModel(dishEntity)).thenReturn(dishModel);

        DishModel result = dishJpaAdapter.saveDish(modelWithId);

        assertNotNull(result);
        verify(dishRepository).findById(99L);
    }

    @Test
    void findById_whenExists_returnsModel() {
        when(dishRepository.findById(1L)).thenReturn(Optional.of(dishEntity));
        when(dishEntityMapper.toModel(dishEntity)).thenReturn(dishModel);

        DishModel result = dishJpaAdapter.findById(1L);

        assertNotNull(result);
        verify(dishRepository).findById(1L);
    }

    @Test
    void findById_whenNotExists_returnsNull() {
        when(dishRepository.findById(99L)).thenReturn(Optional.empty());

        DishModel result = dishJpaAdapter.findById(99L);

        assertNull(result);
        verify(dishEntityMapper, never()).toModel(any());
    }

    @Test
    void findByRestaurantId_returnsPageOfModels() {
        Page<DishEntity> entityPage = new PageImpl<>(List.of(dishEntity));
        when(dishRepository.findByRestaurantId(1L, PageRequest.of(0, 10))).thenReturn(entityPage);
        when(dishEntityMapper.toModel(dishEntity)).thenReturn(dishModel);

        Page<DishModel> result = dishJpaAdapter.findByRestaurantId(1L, 0, 10);

        assertEquals(1, result.getContent().size());
        verify(dishRepository).findByRestaurantId(1L, PageRequest.of(0, 10));
    }

    @Test
    void findByRestaurantIdAndCategoryId_returnsPageOfModels() {
        Page<DishEntity> entityPage = new PageImpl<>(List.of(dishEntity));
        when(dishRepository.findByRestaurantIdAndCategoryId(1L, 2L, PageRequest.of(0, 10))).thenReturn(entityPage);
        when(dishEntityMapper.toModel(dishEntity)).thenReturn(dishModel);

        Page<DishModel> result = dishJpaAdapter.findByRestaurantIdAndCategoryId(1L, 2L, 0, 10);

        assertEquals(1, result.getContent().size());
        verify(dishRepository).findByRestaurantIdAndCategoryId(1L, 2L, PageRequest.of(0, 10));
    }
}
