package com.gastropolis.plazoleta.domain.usecase;

import com.gastropolis.plazoleta.domain.api.IRestaurantServicePort;
import com.gastropolis.plazoleta.domain.exception.RestaurantNotFoundException;
import com.gastropolis.plazoleta.domain.model.CreateEmployeeModel;
import com.gastropolis.plazoleta.domain.model.RestaurantModel;
import com.gastropolis.plazoleta.domain.spi.IEmployeeRestaurantPersistencePort;
import com.gastropolis.plazoleta.domain.spi.IRestaurantPersistencePort;
import com.gastropolis.plazoleta.domain.spi.IUserClientPort;
import org.springframework.data.domain.Page;

public class RestaurantUseCase implements IRestaurantServicePort {

    private static final String ROLE_PROPIETARIO = "PROPIETARIO";

    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final IUserClientPort userClientPort;
    private final IEmployeeRestaurantPersistencePort employeeRestaurantPersistencePort;

    public RestaurantUseCase(IRestaurantPersistencePort restaurantPersistencePort,
                             IUserClientPort userClientPort,
                             IEmployeeRestaurantPersistencePort employeeRestaurantPersistencePort) {
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.userClientPort = userClientPort;
        this.employeeRestaurantPersistencePort = employeeRestaurantPersistencePort;
    }

    @Override
    public RestaurantModel createRestaurant(RestaurantModel restaurantModel, String token) {
        if (restaurantModel.getName().matches("^\\d+$")) {
            throw new IllegalArgumentException("El nombre del restaurante no puede ser solo números");
        }

        String ownerRole = userClientPort.getUserRoleById(restaurantModel.getOwnerId(), token);
        if (!ROLE_PROPIETARIO.equalsIgnoreCase(ownerRole)) {
            throw new IllegalArgumentException("El usuario indicado no tiene el rol PROPIETARIO");
        }

        return restaurantPersistencePort.saveRestaurant(restaurantModel);
    }

    @Override
    public Page<RestaurantModel> listRestaurants(int page, int size) {
        return restaurantPersistencePort.findAllRestaurantsSortedByName(page, size);
    }

    @Override
    public RestaurantModel getRestaurantById(Long id) {
        RestaurantModel restaurant = restaurantPersistencePort.findById(id);
        if (restaurant == null) {
            throw new RestaurantNotFoundException();
        }
        return restaurant;
    }

    @Override
    public void createEmployee(CreateEmployeeModel model, Long ownerId, String token) {
        RestaurantModel restaurant = restaurantPersistencePort.findByOwnerId(ownerId);
        if (restaurant == null) {
            throw new RestaurantNotFoundException();
        }

        Long employeeId = userClientPort.createEmployee(model, token);

        employeeRestaurantPersistencePort.saveEmployeeRestaurant(employeeId, restaurant.getId());
    }
}
