package com.gastropolis.plazoleta.application.mapper;

import com.gastropolis.plazoleta.application.dto.request.CreateRestaurantRequestDto;
import com.gastropolis.plazoleta.domain.model.RestaurantModel;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-23T18:19:28-0500",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.4.jar, environment: Java 21.0.9 (Oracle Corporation)"
)
@Component
public class IRestaurantRequestMapperImpl implements IRestaurantRequestMapper {

    @Override
    public RestaurantModel toRestaurantModel(CreateRestaurantRequestDto dto) {
        if ( dto == null ) {
            return null;
        }

        RestaurantModel restaurantModel = new RestaurantModel();

        restaurantModel.setName( dto.getName() );
        restaurantModel.setNit( dto.getNit() );
        restaurantModel.setAddress( dto.getAddress() );
        restaurantModel.setPhone( dto.getPhone() );
        restaurantModel.setLogoUrl( dto.getLogoUrl() );
        restaurantModel.setOwnerId( dto.getOwnerId() );

        return restaurantModel;
    }
}
