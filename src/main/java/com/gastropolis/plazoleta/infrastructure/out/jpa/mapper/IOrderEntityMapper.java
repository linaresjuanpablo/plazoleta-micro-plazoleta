package com.gastropolis.plazoleta.infrastructure.out.jpa.mapper;

import com.gastropolis.plazoleta.domain.model.OrderDishModel;
import com.gastropolis.plazoleta.domain.model.OrderModel;
import com.gastropolis.plazoleta.infrastructure.out.jpa.entity.OrderDishEntity;
import com.gastropolis.plazoleta.infrastructure.out.jpa.entity.OrderEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IOrderEntityMapper {

    @Mapping(target = "restaurant", ignore = true)
    @Mapping(target = "orderDishes", ignore = true)
    OrderEntity toEntity(OrderModel model);

    @Mapping(target = "dishes", source = "orderDishes")
    @Mapping(target = "restaurantName", source = "restaurant.name")
    OrderModel toModel(OrderEntity entity);

    @Mapping(target = "orderId", source = "id.orderId")
    @Mapping(target = "dishId", source = "id.dishId")
    @Mapping(target = "dishName", source = "dish.name")
    OrderDishModel orderDishEntityToModel(OrderDishEntity entity);

    List<OrderModel> toModelList(List<OrderEntity> entities);
}
