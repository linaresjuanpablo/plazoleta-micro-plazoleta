package com.gastropolis.plazoleta.application.mapper;

import com.gastropolis.plazoleta.application.dto.response.OrderDishResponseDto;
import com.gastropolis.plazoleta.application.dto.response.OrderResponseDto;
import com.gastropolis.plazoleta.domain.model.OrderDishModel;
import com.gastropolis.plazoleta.domain.model.OrderModel;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IOrderResponseMapper {
    OrderResponseDto toOrderResponseDto(OrderModel model);
    OrderDishResponseDto toOrderDishResponseDto(OrderDishModel model);
    List<OrderResponseDto> toOrderResponseDtoList(List<OrderModel> models);
}
