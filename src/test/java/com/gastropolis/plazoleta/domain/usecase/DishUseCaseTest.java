package com.gastropolis.plazoleta.domain.usecase;

import com.gastropolis.plazoleta.domain.exception.DishNotFoundException;
import com.gastropolis.plazoleta.domain.exception.NotRestaurantOwnerException;
import com.gastropolis.plazoleta.domain.exception.RestaurantNotFoundException;
import com.gastropolis.plazoleta.domain.model.DishModel;
import com.gastropolis.plazoleta.domain.model.RestaurantModel;
import com.gastropolis.plazoleta.domain.spi.IDishPersistencePort;
import com.gastropolis.plazoleta.domain.spi.IRestaurantPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DishUseCaseTest {

    @Mock
    private IDishPersistencePort dishPersistencePort;

    @Mock
    private IRestaurantPersistencePort restaurantPersistencePort;

    @InjectMocks
    private DishUseCase dishUseCase;

    private RestaurantModel restaurant;
    private DishModel dish;

    @BeforeEach
    void setUp() {
        restaurant = new RestaurantModel(1L, "Burger House", "123456789", "Calle 10", "+573001234567", "http://logo.png", 20L);
        dish = new DishModel(10L, "Hamburguesa", "Desc", 15000, "http://img.png", true, 2L, 1L);
    }

    // ---- createDish ----

    @Test
    void createDish_whenRestaurantNotFound_throwsRestaurantNotFoundException() {
        when(restaurantPersistencePort.findById(1L)).thenReturn(null);

        assertThrows(RestaurantNotFoundException.class,
                () -> dishUseCase.createDish(dish, 20L));

        verifyNoInteractions(dishPersistencePort);
    }

    @Test
    void createDish_whenNotOwner_throwsNotRestaurantOwnerException() {
        when(restaurantPersistencePort.findById(1L)).thenReturn(restaurant);

        assertThrows(NotRestaurantOwnerException.class,
                () -> dishUseCase.createDish(dish, 99L));

        verifyNoInteractions(dishPersistencePort);
    }

    @Test
    void createDish_happyPath_setsActiveAndSaves() {
        dish.setActive(false);
        DishModel saved = new DishModel(10L, "Hamburguesa", "Desc", 15000, "http://img.png", true, 2L, 1L);
        when(restaurantPersistencePort.findById(1L)).thenReturn(restaurant);
        when(dishPersistencePort.saveDish(dish)).thenReturn(saved);

        DishModel result = dishUseCase.createDish(dish, 20L);

        assertTrue(dish.getActive());
        assertEquals(saved, result);
        verify(dishPersistencePort).saveDish(dish);
    }

    // ---- updateDish ----

    @Test
    void updateDish_whenDishNotFound_throwsDishNotFoundException() {
        when(dishPersistencePort.findById(10L)).thenReturn(null);

        assertThrows(DishNotFoundException.class,
                () -> dishUseCase.updateDish(10L, 20000, "New desc", 20L));

        verifyNoInteractions(restaurantPersistencePort);
    }

    @Test
    void updateDish_whenRestaurantNotFound_throwsRestaurantNotFoundException() {
        when(dishPersistencePort.findById(10L)).thenReturn(dish);
        when(restaurantPersistencePort.findById(1L)).thenReturn(null);

        assertThrows(RestaurantNotFoundException.class,
                () -> dishUseCase.updateDish(10L, 20000, "New desc", 20L));
    }

    @Test
    void updateDish_whenNotOwner_throwsNotRestaurantOwnerException() {
        when(dishPersistencePort.findById(10L)).thenReturn(dish);
        when(restaurantPersistencePort.findById(1L)).thenReturn(restaurant);

        assertThrows(NotRestaurantOwnerException.class,
                () -> dishUseCase.updateDish(10L, 20000, "New desc", 99L));
    }

    @Test
    void updateDish_withPriceAndDescription_updatesAndSaves() {
        when(dishPersistencePort.findById(10L)).thenReturn(dish);
        when(restaurantPersistencePort.findById(1L)).thenReturn(restaurant);
        when(dishPersistencePort.saveDish(dish)).thenReturn(dish);

        DishModel result = dishUseCase.updateDish(10L, 25000, "Updated desc", 20L);

        assertEquals(25000, dish.getPrice());
        assertEquals("Updated desc", dish.getDescription());
        verify(dishPersistencePort).saveDish(dish);
        assertNotNull(result);
    }

    @Test
    void updateDish_withNullPrice_doesNotChangePrice() {
        dish.setPrice(15000);
        dish.setDescription("Old desc");
        when(dishPersistencePort.findById(10L)).thenReturn(dish);
        when(restaurantPersistencePort.findById(1L)).thenReturn(restaurant);
        when(dishPersistencePort.saveDish(dish)).thenReturn(dish);

        dishUseCase.updateDish(10L, null, "New desc", 20L);

        assertEquals(15000, dish.getPrice());
        assertEquals("New desc", dish.getDescription());
    }

    @Test
    void updateDish_withNullDescription_doesNotChangeDescription() {
        dish.setPrice(15000);
        dish.setDescription("Old desc");
        when(dishPersistencePort.findById(10L)).thenReturn(dish);
        when(restaurantPersistencePort.findById(1L)).thenReturn(restaurant);
        when(dishPersistencePort.saveDish(dish)).thenReturn(dish);

        dishUseCase.updateDish(10L, 30000, null, 20L);

        assertEquals(30000, dish.getPrice());
        assertEquals("Old desc", dish.getDescription());
    }

    @Test
    void updateDish_withBothNull_doesNotChangeAnything() {
        dish.setPrice(15000);
        dish.setDescription("Old desc");
        when(dishPersistencePort.findById(10L)).thenReturn(dish);
        when(restaurantPersistencePort.findById(1L)).thenReturn(restaurant);
        when(dishPersistencePort.saveDish(dish)).thenReturn(dish);

        dishUseCase.updateDish(10L, null, null, 20L);

        assertEquals(15000, dish.getPrice());
        assertEquals("Old desc", dish.getDescription());
    }

    // ---- toggleDishActive ----

    @Test
    void toggleDishActive_whenDishNotFound_throwsDishNotFoundException() {
        when(dishPersistencePort.findById(10L)).thenReturn(null);

        assertThrows(DishNotFoundException.class,
                () -> dishUseCase.toggleDishActive(10L, 20L));
    }

    @Test
    void toggleDishActive_whenRestaurantNotFound_throwsRestaurantNotFoundException() {
        when(dishPersistencePort.findById(10L)).thenReturn(dish);
        when(restaurantPersistencePort.findById(1L)).thenReturn(null);

        assertThrows(RestaurantNotFoundException.class,
                () -> dishUseCase.toggleDishActive(10L, 20L));
    }

    @Test
    void toggleDishActive_whenNotOwner_throwsNotRestaurantOwnerException() {
        when(dishPersistencePort.findById(10L)).thenReturn(dish);
        when(restaurantPersistencePort.findById(1L)).thenReturn(restaurant);

        assertThrows(NotRestaurantOwnerException.class,
                () -> dishUseCase.toggleDishActive(10L, 99L));
    }

    @Test
    void toggleDishActive_whenActiveTrue_setsToFalseAndSaves() {
        dish.setActive(true);
        when(dishPersistencePort.findById(10L)).thenReturn(dish);
        when(restaurantPersistencePort.findById(1L)).thenReturn(restaurant);
        when(dishPersistencePort.saveDish(dish)).thenReturn(dish);

        DishModel result = dishUseCase.toggleDishActive(10L, 20L);

        assertFalse(dish.getActive());
        verify(dishPersistencePort).saveDish(dish);
        assertNotNull(result);
    }

    @Test
    void toggleDishActive_whenActiveFalse_setsToTrueAndSaves() {
        dish.setActive(false);
        when(dishPersistencePort.findById(10L)).thenReturn(dish);
        when(restaurantPersistencePort.findById(1L)).thenReturn(restaurant);
        when(dishPersistencePort.saveDish(dish)).thenReturn(dish);

        DishModel result = dishUseCase.toggleDishActive(10L, 20L);

        assertTrue(dish.getActive());
        verify(dishPersistencePort).saveDish(dish);
        assertNotNull(result);
    }

    // ---- listDishesByRestaurant ----

    @Test
    void listDishesByRestaurant_withCategoryId_callsFindByRestaurantIdAndCategoryId() {
        Page<DishModel> page = new PageImpl<>(List.of(dish));
        when(dishPersistencePort.findByRestaurantIdAndCategoryId(1L, 2L, 0, 10)).thenReturn(page);

        Page<DishModel> result = dishUseCase.listDishesByRestaurant(1L, 2L, 0, 10);

        assertEquals(1, result.getContent().size());
        verify(dishPersistencePort).findByRestaurantIdAndCategoryId(1L, 2L, 0, 10);
        verify(dishPersistencePort, never()).findByRestaurantId(anyLong(), anyInt(), anyInt());
    }

    @Test
    void listDishesByRestaurant_withNullCategoryId_callsFindByRestaurantId() {
        Page<DishModel> page = new PageImpl<>(List.of(dish));
        when(dishPersistencePort.findByRestaurantId(1L, 0, 10)).thenReturn(page);

        Page<DishModel> result = dishUseCase.listDishesByRestaurant(1L, null, 0, 10);

        assertEquals(1, result.getContent().size());
        verify(dishPersistencePort).findByRestaurantId(1L, 0, 10);
        verify(dishPersistencePort, never()).findByRestaurantIdAndCategoryId(anyLong(), anyLong(), anyInt(), anyInt());
    }

    // ---- getDishById ----

    @Test
    void getDishById_whenNotFound_throwsDishNotFoundException() {
        when(dishPersistencePort.findById(99L)).thenReturn(null);

        assertThrows(DishNotFoundException.class,
                () -> dishUseCase.getDishById(99L));
    }

    @Test
    void getDishById_whenFound_returnsDish() {
        when(dishPersistencePort.findById(10L)).thenReturn(dish);

        DishModel result = dishUseCase.getDishById(10L);

        assertEquals(dish, result);
    }
}
