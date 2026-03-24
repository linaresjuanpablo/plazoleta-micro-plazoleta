package com.gastropolis.plazoleta.infrastructure.out.jpa.adapter;

import com.gastropolis.plazoleta.domain.model.OrderDishModel;
import com.gastropolis.plazoleta.domain.model.OrderModel;
import com.gastropolis.plazoleta.domain.spi.IOrderPersistencePort;
import com.gastropolis.plazoleta.infrastructure.out.jpa.entity.*;
import com.gastropolis.plazoleta.infrastructure.out.jpa.mapper.IOrderEntityMapper;
import com.gastropolis.plazoleta.infrastructure.out.jpa.repository.IDishRepository;
import com.gastropolis.plazoleta.infrastructure.out.jpa.repository.IEmployeeRestaurantRepository;
import com.gastropolis.plazoleta.infrastructure.out.jpa.repository.IOrderRepository;
import com.gastropolis.plazoleta.infrastructure.out.jpa.repository.IRestaurantRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;

public class OrderJpaAdapter implements IOrderPersistencePort {

    private final IOrderRepository orderRepository;
    private final IOrderEntityMapper orderEntityMapper;
    private final IRestaurantRepository restaurantRepository;
    private final IDishRepository dishRepository;
    private final IEmployeeRestaurantRepository employeeRestaurantRepository;

    public OrderJpaAdapter(IOrderRepository orderRepository, IOrderEntityMapper orderEntityMapper,
                           IRestaurantRepository restaurantRepository, IDishRepository dishRepository,
                           IEmployeeRestaurantRepository employeeRestaurantRepository) {
        this.orderRepository = orderRepository;
        this.orderEntityMapper = orderEntityMapper;
        this.restaurantRepository = restaurantRepository;
        this.dishRepository = dishRepository;
        this.employeeRestaurantRepository = employeeRestaurantRepository;
    }

    @Override
    public OrderModel saveOrder(OrderModel orderModel) {
        OrderEntity entity;

        if (orderModel.getId() != null) {
            entity = orderRepository.findById(orderModel.getId()).orElse(new OrderEntity());
            entity.setStatus(orderModel.getStatus());
            entity.setEmployeeId(orderModel.getEmployeeId());
            entity.setSecurityPin(orderModel.getSecurityPin());
            OrderEntity saved = orderRepository.save(entity);
            return orderEntityMapper.toModel(saved);
        }

        entity = new OrderEntity();
        entity.setDate(orderModel.getDate());
        entity.setStatus(orderModel.getStatus());
        entity.setClientId(orderModel.getClientId());
        entity.setEmployeeId(orderModel.getEmployeeId());
        entity.setSecurityPin(orderModel.getSecurityPin());

        RestaurantEntity restaurant = restaurantRepository.getReferenceById(orderModel.getRestaurantId());
        entity.setRestaurant(restaurant);
        entity.setOrderDishes(new ArrayList<>());

        OrderEntity savedOrder = orderRepository.save(entity);

        if (orderModel.getDishes() != null) {
            for (OrderDishModel dishModel : orderModel.getDishes()) {
                OrderDishEntity orderDishEntity = new OrderDishEntity();
                OrderDishId dishId = new OrderDishId(savedOrder.getId(), dishModel.getDishId());
                orderDishEntity.setId(dishId);
                orderDishEntity.setOrder(savedOrder);
                DishEntity dishEntity = dishRepository.getReferenceById(dishModel.getDishId());
                orderDishEntity.setDish(dishEntity);
                orderDishEntity.setQuantity(dishModel.getQuantity());
                savedOrder.getOrderDishes().add(orderDishEntity);
            }
            entity = orderRepository.save(savedOrder);
        } else {
            entity = savedOrder;
        }

        return orderEntityMapper.toModel(entity);
    }

    @Override
    public OrderModel findById(Long id) {
        return orderRepository.findById(id)
                .map(orderEntityMapper::toModel)
                .orElse(null);
    }

    @Override
    public Page<OrderModel> findByRestaurantId(Long restaurantId, int page, int size) {
        Page<OrderEntity> entityPage = orderRepository.findByRestaurantId(restaurantId, PageRequest.of(page, size));
        return entityPage.map(orderEntityMapper::toModel);
    }

    @Override
    public Page<OrderModel> findByRestaurantIdAndStatus(Long restaurantId, String status, int page, int size) {
        Page<OrderEntity> entityPage = orderRepository.findByRestaurantIdAndStatus(restaurantId, status, PageRequest.of(page, size));
        return entityPage.map(orderEntityMapper::toModel);
    }

    @Override
    public List<OrderModel> findActiveOrdersByClientId(Long clientId, List<String> statuses) {
        List<OrderEntity> entities = orderRepository.findByClientIdAndStatusIn(clientId, statuses);
        return orderEntityMapper.toModelList(entities);
    }

    @Override
    public Long findRestaurantIdByEmployeeId(Long employeeId) {
        return employeeRestaurantRepository.findByIdEmployeeId(employeeId)
                .map(e -> e.getId().getRestaurantId())
                .orElse(null);
    }
}
