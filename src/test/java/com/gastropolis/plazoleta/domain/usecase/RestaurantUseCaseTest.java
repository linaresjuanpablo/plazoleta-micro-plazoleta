package com.gastropolis.plazoleta.domain.usecase;

import com.gastropolis.plazoleta.domain.exception.RestaurantNotFoundException;
import com.gastropolis.plazoleta.domain.model.CreateEmployeeModel;
import com.gastropolis.plazoleta.domain.model.RestaurantModel;
import com.gastropolis.plazoleta.domain.spi.IEmployeeRestaurantPersistencePort;
import com.gastropolis.plazoleta.domain.spi.IRestaurantPersistencePort;
import com.gastropolis.plazoleta.domain.spi.IUserClientPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestaurantUseCaseTest {

    @Mock
    private IRestaurantPersistencePort restaurantPersistencePort;

    @Mock
    private IUserClientPort userClientPort;

    @Mock
    private IEmployeeRestaurantPersistencePort employeeRestaurantPersistencePort;

    @InjectMocks
    private RestaurantUseCase restaurantUseCase;

    private RestaurantModel restaurantModel;

    @BeforeEach
    void setUp() {
        restaurantModel = new RestaurantModel(1L, "Burger House", "123456789", "Calle 10", "+573001234567", "http://logo.png", 10L);
    }

    // ---- createRestaurant ----

    @Test
    void createRestaurant_whenNameIsAllDigits_throwsIllegalArgumentException() {
        restaurantModel.setName("12345");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> restaurantUseCase.createRestaurant(restaurantModel, "token"));

        assertEquals("El nombre del restaurante no puede ser solo números", ex.getMessage());
        verifyNoInteractions(userClientPort, restaurantPersistencePort);
    }

    @Test
    void createRestaurant_whenRoleIsNotPropietario_throwsIllegalArgumentException() {
        when(userClientPort.getUserRoleById(10L, "token")).thenReturn("EMPLEADO");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> restaurantUseCase.createRestaurant(restaurantModel, "token"));

        assertEquals("El usuario indicado no tiene el rol PROPIETARIO", ex.getMessage());
        verify(userClientPort).getUserRoleById(10L, "token");
        verifyNoInteractions(restaurantPersistencePort);
    }

    @Test
    void createRestaurant_whenRoleIsPropietarioLowercase_succeeds() {
        RestaurantModel saved = new RestaurantModel(1L, "Burger House", "123456789", "Calle 10", "+573001234567", "http://logo.png", 10L);
        when(userClientPort.getUserRoleById(10L, "token")).thenReturn("propietario");
        when(restaurantPersistencePort.saveRestaurant(restaurantModel)).thenReturn(saved);

        RestaurantModel result = restaurantUseCase.createRestaurant(restaurantModel, "token");

        assertNotNull(result);
        assertEquals(saved, result);
        verify(restaurantPersistencePort).saveRestaurant(restaurantModel);
    }

    @Test
    void createRestaurant_happyPath_savesAndReturnsRestaurant() {
        RestaurantModel saved = new RestaurantModel(1L, "Burger House", "123456789", "Calle 10", "+573001234567", "http://logo.png", 10L);
        when(userClientPort.getUserRoleById(10L, "token")).thenReturn("PROPIETARIO");
        when(restaurantPersistencePort.saveRestaurant(restaurantModel)).thenReturn(saved);

        RestaurantModel result = restaurantUseCase.createRestaurant(restaurantModel, "token");

        assertNotNull(result);
        assertEquals(saved.getId(), result.getId());
        verify(userClientPort).getUserRoleById(10L, "token");
        verify(restaurantPersistencePort).saveRestaurant(restaurantModel);
    }

    // ---- listRestaurants ----

    @Test
    void listRestaurants_delegatesToPersistencePort() {
        Page<RestaurantModel> page = new PageImpl<>(List.of(restaurantModel));
        when(restaurantPersistencePort.findAllRestaurantsSortedByName(0, 10)).thenReturn(page);

        Page<RestaurantModel> result = restaurantUseCase.listRestaurants(0, 10);

        assertEquals(1, result.getContent().size());
        verify(restaurantPersistencePort).findAllRestaurantsSortedByName(0, 10);
    }

    // ---- getRestaurantById ----

    @Test
    void getRestaurantById_whenNotFound_throwsRestaurantNotFoundException() {
        when(restaurantPersistencePort.findById(99L)).thenReturn(null);

        assertThrows(RestaurantNotFoundException.class,
                () -> restaurantUseCase.getRestaurantById(99L));
    }

    @Test
    void getRestaurantById_whenFound_returnsRestaurant() {
        when(restaurantPersistencePort.findById(1L)).thenReturn(restaurantModel);

        RestaurantModel result = restaurantUseCase.getRestaurantById(1L);

        assertEquals(restaurantModel, result);
    }

    // ---- createEmployee ----

    @Test
    void createEmployee_whenRestaurantNotFoundForOwner_throwsRestaurantNotFoundException() {
        when(restaurantPersistencePort.findByOwnerId(10L)).thenReturn(null);

        CreateEmployeeModel employeeModel = new CreateEmployeeModel(
                "Juan", "Perez", "123456", "+573001111111", LocalDate.of(1990, 1, 1), "juan@mail.com", "secret123"
        );

        assertThrows(RestaurantNotFoundException.class,
                () -> restaurantUseCase.createEmployee(employeeModel, 10L, "token"));

        verifyNoInteractions(userClientPort, employeeRestaurantPersistencePort);
    }

    @Test
    void createEmployee_happyPath_createsEmployeeAndSavesRelation() {
        when(restaurantPersistencePort.findByOwnerId(10L)).thenReturn(restaurantModel);
        when(userClientPort.createEmployee(any(CreateEmployeeModel.class), eq("token"))).thenReturn(55L);

        CreateEmployeeModel employeeModel = new CreateEmployeeModel(
                "Juan", "Perez", "123456", "+573001111111", LocalDate.of(1990, 1, 1), "juan@mail.com", "secret123"
        );

        restaurantUseCase.createEmployee(employeeModel, 10L, "token");

        verify(userClientPort).createEmployee(employeeModel, "token");
        verify(employeeRestaurantPersistencePort).saveEmployeeRestaurant(55L, 1L);
    }
}
