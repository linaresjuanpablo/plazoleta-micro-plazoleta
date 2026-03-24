package com.gastropolis.plazoleta.application.mapper;

import com.gastropolis.plazoleta.application.dto.request.CreateDishRequestDto;
import com.gastropolis.plazoleta.domain.model.DishModel;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-23T18:19:29-0500",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.4.jar, environment: Java 21.0.9 (Oracle Corporation)"
)
@Component
public class IDishRequestMapperImpl implements IDishRequestMapper {

    @Override
    public DishModel toDishModel(CreateDishRequestDto dto) {
        if ( dto == null ) {
            return null;
        }

        DishModel dishModel = new DishModel();

        dishModel.setName( dto.getName() );
        dishModel.setDescription( dto.getDescription() );
        dishModel.setPrice( dto.getPrice() );
        dishModel.setImageUrl( dto.getImageUrl() );
        dishModel.setCategoryId( dto.getCategoryId() );
        dishModel.setRestaurantId( dto.getRestaurantId() );

        return dishModel;
    }
}
