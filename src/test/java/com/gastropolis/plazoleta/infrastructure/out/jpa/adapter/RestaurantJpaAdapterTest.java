package com.gastropolis.plazoleta.infrastructure.out.jpa.adapter;

import com.gastropolis.plazoleta.domain.model.RestaurantModel;
import com.gastropolis.plazoleta.infrastructure.out.jpa.entity.RestaurantEntity;
import com.gastropolis.plazoleta.infrastructure.out.jpa.mapper.IRestaurantEntityMapper;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestaurantJpaAdapterTest {

    @Mock
    private IRestaurantRepository restaurantRepository;

    @Mock
    private IRestaurantEntityMapper restaurantEntityMapper;

    private RestaurantJpaAdapter restaurantJpaAdapter;

    private RestaurantModel restaurantModel;
    private RestaurantEntity restaurantEntity;

    @BeforeEach
    void setUp() {
        restaurantJpaAdapter = new RestaurantJpaAdapter(restaurantRepository, restaurantEntityMapper);

        restaurantModel = new RestaurantModel(1L, "Burger House", "123456789", "Calle 10",
                "+573001234567", "http://logo.png", 20L);
        restaurantEntity = new RestaurantEntity();
        restaurantEntity.setId(1L);
        restaurantEntity.setName("Burger House");
    }

    @Test
    void saveRestaurant_mapsToEntitySavesAndReturnsModel() {
        when(restaurantEntityMapper.toEntity(restaurantModel)).thenReturn(restaurantEntity);
        when(restaurantRepository.save(restaurantEntity)).thenReturn(restaurantEntity);
        when(restaurantEntityMapper.toModel(restaurantEntity)).thenReturn(restaurantModel);

        RestaurantModel result = restaurantJpaAdapter.saveRestaurant(restaurantModel);

        assertNotNull(result);
        assertEquals("Burger House", result.getName());
        verify(restaurantEntityMapper).toEntity(restaurantModel);
        verify(restaurantRepository).save(restaurantEntity);
        verify(restaurantEntityMapper).toModel(restaurantEntity);
    }

    @Test
    void findAllRestaurantsSortedByName_returnsPageOfModels() {
        Page<RestaurantEntity> entityPage = new PageImpl<>(List.of(restaurantEntity));
        when(restaurantRepository.findAllByOrderByNameAsc(PageRequest.of(0, 10))).thenReturn(entityPage);
        when(restaurantEntityMapper.toModel(restaurantEntity)).thenReturn(restaurantModel);

        Page<RestaurantModel> result = restaurantJpaAdapter.findAllRestaurantsSortedByName(0, 10);

        assertEquals(1, result.getContent().size());
        assertEquals("Burger House", result.getContent().get(0).getName());
        verify(restaurantRepository).findAllByOrderByNameAsc(PageRequest.of(0, 10));
    }

    @Test
    void findById_whenExists_returnsModel() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurantEntity));
        when(restaurantEntityMapper.toModel(restaurantEntity)).thenReturn(restaurantModel);

        RestaurantModel result = restaurantJpaAdapter.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(restaurantRepository).findById(1L);
    }

    @Test
    void findById_whenNotExists_returnsNull() {
        when(restaurantRepository.findById(99L)).thenReturn(Optional.empty());

        RestaurantModel result = restaurantJpaAdapter.findById(99L);

        assertNull(result);
        verify(restaurantRepository).findById(99L);
        verify(restaurantEntityMapper, never()).toModel(any());
    }

    @Test
    void findByOwnerId_whenExists_returnsModel() {
        when(restaurantRepository.findByOwnerId(20L)).thenReturn(Optional.of(restaurantEntity));
        when(restaurantEntityMapper.toModel(restaurantEntity)).thenReturn(restaurantModel);

        RestaurantModel result = restaurantJpaAdapter.findByOwnerId(20L);

        assertNotNull(result);
        assertEquals(20L, result.getOwnerId());
        verify(restaurantRepository).findByOwnerId(20L);
    }

    @Test
    void findByOwnerId_whenNotExists_returnsNull() {
        when(restaurantRepository.findByOwnerId(99L)).thenReturn(Optional.empty());

        RestaurantModel result = restaurantJpaAdapter.findByOwnerId(99L);

        assertNull(result);
        verify(restaurantRepository).findByOwnerId(99L);
        verify(restaurantEntityMapper, never()).toModel(any());
    }
}
