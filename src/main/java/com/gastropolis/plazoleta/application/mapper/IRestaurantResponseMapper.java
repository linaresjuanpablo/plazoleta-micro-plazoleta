package com.gastropolis.plazoleta.application.mapper;

import com.gastropolis.plazoleta.application.dto.response.RestaurantResponseDto;
import com.gastropolis.plazoleta.domain.model.RestaurantModel;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IRestaurantResponseMapper {
    RestaurantResponseDto toRestaurantResponseDto(RestaurantModel model);
    List<RestaurantResponseDto> toRestaurantResponseDtoList(List<RestaurantModel> models);
}
