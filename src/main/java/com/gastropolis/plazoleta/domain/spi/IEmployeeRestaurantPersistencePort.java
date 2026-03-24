package com.gastropolis.plazoleta.domain.spi;

public interface IEmployeeRestaurantPersistencePort {
    void saveEmployeeRestaurant(Long employeeId, Long restaurantId);
}
