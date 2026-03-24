package com.gastropolis.plazoleta.application.mapper;

import com.gastropolis.plazoleta.application.dto.response.OrderDishResponseDto;
import com.gastropolis.plazoleta.application.dto.response.OrderResponseDto;
import com.gastropolis.plazoleta.domain.model.OrderDishModel;
import com.gastropolis.plazoleta.domain.model.OrderModel;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-23T18:19:29-0500",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.4.jar, environment: Java 21.0.9 (Oracle Corporation)"
)
@Component
public class IOrderResponseMapperImpl implements IOrderResponseMapper {

    @Override
    public OrderResponseDto toOrderResponseDto(OrderModel model) {
        if ( model == null ) {
            return null;
        }

        OrderResponseDto orderResponseDto = new OrderResponseDto();

        orderResponseDto.setId( model.getId() );
        orderResponseDto.setDate( model.getDate() );
        orderResponseDto.setStatus( model.getStatus() );
        orderResponseDto.setClientId( model.getClientId() );
        orderResponseDto.setClientName( model.getClientName() );
        orderResponseDto.setRestaurantId( model.getRestaurantId() );
        orderResponseDto.setRestaurantName( model.getRestaurantName() );
        orderResponseDto.setSecurityPin( model.getSecurityPin() );
        orderResponseDto.setDishes( orderDishModelListToOrderDishResponseDtoList( model.getDishes() ) );

        return orderResponseDto;
    }

    @Override
    public OrderDishResponseDto toOrderDishResponseDto(OrderDishModel model) {
        if ( model == null ) {
            return null;
        }

        OrderDishResponseDto orderDishResponseDto = new OrderDishResponseDto();

        orderDishResponseDto.setDishId( model.getDishId() );
        orderDishResponseDto.setDishName( model.getDishName() );
        orderDishResponseDto.setQuantity( model.getQuantity() );

        return orderDishResponseDto;
    }

    @Override
    public List<OrderResponseDto> toOrderResponseDtoList(List<OrderModel> models) {
        if ( models == null ) {
            return null;
        }

        List<OrderResponseDto> list = new ArrayList<OrderResponseDto>( models.size() );
        for ( OrderModel orderModel : models ) {
            list.add( toOrderResponseDto( orderModel ) );
        }

        return list;
    }

    protected List<OrderDishResponseDto> orderDishModelListToOrderDishResponseDtoList(List<OrderDishModel> list) {
        if ( list == null ) {
            return null;
        }

        List<OrderDishResponseDto> list1 = new ArrayList<OrderDishResponseDto>( list.size() );
        for ( OrderDishModel orderDishModel : list ) {
            list1.add( toOrderDishResponseDto( orderDishModel ) );
        }

        return list1;
    }
}
