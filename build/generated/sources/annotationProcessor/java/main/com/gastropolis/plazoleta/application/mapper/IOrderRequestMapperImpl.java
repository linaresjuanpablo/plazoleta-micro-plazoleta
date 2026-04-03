package com.gastropolis.plazoleta.application.mapper;

import com.gastropolis.plazoleta.application.dto.request.CreateOrderRequestDto;
import com.gastropolis.plazoleta.application.dto.request.OrderDishRequestDto;
import com.gastropolis.plazoleta.domain.model.OrderDishModel;
import com.gastropolis.plazoleta.domain.model.OrderModel;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-30T17:35:03-0500",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.4.jar, environment: Java 17.0.9 (Amazon.com Inc.)"
)
@Component
public class IOrderRequestMapperImpl implements IOrderRequestMapper {

    @Override
    public OrderModel toOrderModel(CreateOrderRequestDto dto) {
        if ( dto == null ) {
            return null;
        }

        OrderModel orderModel = new OrderModel();

        orderModel.setDishes( orderDishRequestDtoListToOrderDishModelList( dto.getDishes() ) );
        orderModel.setRestaurantId( dto.getRestaurantId() );

        return orderModel;
    }

    @Override
    public OrderDishModel toOrderDishModel(OrderDishRequestDto dto) {
        if ( dto == null ) {
            return null;
        }

        OrderDishModel orderDishModel = new OrderDishModel();

        orderDishModel.setDishId( dto.getDishId() );
        orderDishModel.setQuantity( dto.getQuantity() );

        return orderDishModel;
    }

    protected List<OrderDishModel> orderDishRequestDtoListToOrderDishModelList(List<OrderDishRequestDto> list) {
        if ( list == null ) {
            return null;
        }

        List<OrderDishModel> list1 = new ArrayList<OrderDishModel>( list.size() );
        for ( OrderDishRequestDto orderDishRequestDto : list ) {
            list1.add( toOrderDishModel( orderDishRequestDto ) );
        }

        return list1;
    }
}
