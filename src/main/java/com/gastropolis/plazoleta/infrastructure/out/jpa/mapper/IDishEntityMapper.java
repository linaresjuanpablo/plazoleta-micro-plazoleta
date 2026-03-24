package com.gastropolis.plazoleta.infrastructure.out.jpa.mapper;

import com.gastropolis.plazoleta.domain.model.DishModel;
import com.gastropolis.plazoleta.infrastructure.out.jpa.entity.DishEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IDishEntityMapper {

    @Mapping(target = "category", ignore = true)
    @Mapping(target = "restaurant", ignore = true)
    DishEntity toEntity(DishModel model);

    DishModel toModel(DishEntity entity);

    List<DishModel> toModelList(List<DishEntity> entities);
}
