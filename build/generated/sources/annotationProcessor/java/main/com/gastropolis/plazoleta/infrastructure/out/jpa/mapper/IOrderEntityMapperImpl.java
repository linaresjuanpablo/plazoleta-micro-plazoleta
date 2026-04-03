package com.gastropolis.plazoleta.infrastructure.out.jpa.mapper;

import com.gastropolis.plazoleta.domain.model.OrderDishModel;
import com.gastropolis.plazoleta.domain.model.OrderModel;
import com.gastropolis.plazoleta.infrastructure.out.jpa.entity.DishEntity;
import com.gastropolis.plazoleta.infrastructure.out.jpa.entity.OrderDishEntity;
import com.gastropolis.plazoleta.infrastructure.out.jpa.entity.OrderDishId;
import com.gastropolis.plazoleta.infrastructure.out.jpa.entity.OrderEntity;
import com.gastropolis.plazoleta.infrastructure.out.jpa.entity.RestaurantEntity;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-30T17:35:00-0500",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.4.jar, environment: Java 17.0.9 (Amazon.com Inc.)"
)
@Component
public class IOrderEntityMapperImpl implements IOrderEntityMapper {

    @Override
    public OrderEntity toEntity(OrderModel model) {
        if ( model == null ) {
            return null;
        }

        OrderEntity orderEntity = new OrderEntity();

        orderEntity.setId( model.getId() );
        orderEntity.setDate( model.getDate() );
        orderEntity.setStatus( model.getStatus() );
        orderEntity.setClientId( model.getClientId() );
        orderEntity.setEmployeeId( model.getEmployeeId() );
        orderEntity.setRestaurantId( model.getRestaurantId() );
        orderEntity.setSecurityPin( model.getSecurityPin() );

        return orderEntity;
    }

    @Override
    public OrderModel toModel(OrderEntity entity) {
        if ( entity == null ) {
            return null;
        }

        OrderModel orderModel = new OrderModel();

        orderModel.setDishes( orderDishEntityListToOrderDishModelList( entity.getOrderDishes() ) );
        orderModel.setRestaurantName( entityRestaurantName( entity ) );
        orderModel.setId( entity.getId() );
        orderModel.setDate( entity.getDate() );
        orderModel.setStatus( entity.getStatus() );
        orderModel.setClientId( entity.getClientId() );
        orderModel.setEmployeeId( entity.getEmployeeId() );
        orderModel.setRestaurantId( entity.getRestaurantId() );
        orderModel.setSecurityPin( entity.getSecurityPin() );

        return orderModel;
    }

    @Override
    public OrderDishModel orderDishEntityToModel(OrderDishEntity entity) {
        if ( entity == null ) {
            return null;
        }

        OrderDishModel orderDishModel = new OrderDishModel();

        orderDishModel.setOrderId( entityIdOrderId( entity ) );
        orderDishModel.setDishId( entityIdDishId( entity ) );
        orderDishModel.setDishName( entityDishName( entity ) );
        orderDishModel.setQuantity( entity.getQuantity() );

        return orderDishModel;
    }

    @Override
    public List<OrderModel> toModelList(List<OrderEntity> entities) {
        if ( entities == null ) {
            return null;
        }

        List<OrderModel> list = new ArrayList<OrderModel>( entities.size() );
        for ( OrderEntity orderEntity : entities ) {
            list.add( toModel( orderEntity ) );
        }

        return list;
    }

    protected List<OrderDishModel> orderDishEntityListToOrderDishModelList(List<OrderDishEntity> list) {
        if ( list == null ) {
            return null;
        }

        List<OrderDishModel> list1 = new ArrayList<OrderDishModel>( list.size() );
        for ( OrderDishEntity orderDishEntity : list ) {
            list1.add( orderDishEntityToModel( orderDishEntity ) );
        }

        return list1;
    }

    private String entityRestaurantName(OrderEntity orderEntity) {
        if ( orderEntity == null ) {
            return null;
        }
        RestaurantEntity restaurant = orderEntity.getRestaurant();
        if ( restaurant == null ) {
            return null;
        }
        String name = restaurant.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }

    private Long entityIdOrderId(OrderDishEntity orderDishEntity) {
        if ( orderDishEntity == null ) {
            return null;
        }
        OrderDishId id = orderDishEntity.getId();
        if ( id == null ) {
            return null;
        }
        Long orderId = id.getOrderId();
        if ( orderId == null ) {
            return null;
        }
        return orderId;
    }

    private Long entityIdDishId(OrderDishEntity orderDishEntity) {
        if ( orderDishEntity == null ) {
            return null;
        }
        OrderDishId id = orderDishEntity.getId();
        if ( id == null ) {
            return null;
        }
        Long dishId = id.getDishId();
        if ( dishId == null ) {
            return null;
        }
        return dishId;
    }

    private String entityDishName(OrderDishEntity orderDishEntity) {
        if ( orderDishEntity == null ) {
            return null;
        }
        DishEntity dish = orderDishEntity.getDish();
        if ( dish == null ) {
            return null;
        }
        String name = dish.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }
}
