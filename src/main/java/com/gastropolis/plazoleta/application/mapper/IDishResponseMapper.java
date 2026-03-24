package com.gastropolis.plazoleta.application.mapper;

import com.gastropolis.plazoleta.application.dto.response.DishResponseDto;
import com.gastropolis.plazoleta.domain.model.DishModel;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IDishResponseMapper {
    DishResponseDto toDishResponseDto(DishModel model);
    List<DishResponseDto> toDishResponseDtoList(List<DishModel> models);
}
