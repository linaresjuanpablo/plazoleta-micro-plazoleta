package com.gastropolis.plazoleta.application.mapper;

import com.gastropolis.plazoleta.application.dto.request.CreateRestaurantRequestDto;
import com.gastropolis.plazoleta.domain.model.RestaurantModel;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IRestaurantRequestMapper {
    RestaurantModel toRestaurantModel(CreateRestaurantRequestDto dto);
}
