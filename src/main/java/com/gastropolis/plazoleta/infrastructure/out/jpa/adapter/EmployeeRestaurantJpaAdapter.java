package com.gastropolis.plazoleta.infrastructure.out.jpa.adapter;

import com.gastropolis.plazoleta.domain.spi.IEmployeeRestaurantPersistencePort;
import com.gastropolis.plazoleta.infrastructure.out.jpa.entity.EmployeeRestaurantEntity;
import com.gastropolis.plazoleta.infrastructure.out.jpa.entity.EmployeeRestaurantId;
import com.gastropolis.plazoleta.infrastructure.out.jpa.entity.RestaurantEntity;
import com.gastropolis.plazoleta.infrastructure.out.jpa.repository.IEmployeeRestaurantRepository;
import com.gastropolis.plazoleta.infrastructure.out.jpa.repository.IRestaurantRepository;

public class EmployeeRestaurantJpaAdapter implements IEmployeeRestaurantPersistencePort {

    private final IEmployeeRestaurantRepository employeeRestaurantRepository;
    private final IRestaurantRepository restaurantRepository;

    public EmployeeRestaurantJpaAdapter(IEmployeeRestaurantRepository employeeRestaurantRepository,
                                        IRestaurantRepository restaurantRepository) {
        this.employeeRestaurantRepository = employeeRestaurantRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public void saveEmployeeRestaurant(Long employeeId, Long restaurantId) {
        EmployeeRestaurantId id = new EmployeeRestaurantId(employeeId, restaurantId);
        RestaurantEntity restaurant = restaurantRepository.getReferenceById(restaurantId);
        EmployeeRestaurantEntity entity = new EmployeeRestaurantEntity(id, restaurant);
        employeeRestaurantRepository.save(entity);
    }
}
