package com.gastropolis.plazoleta.application.mapper;

import com.gastropolis.plazoleta.application.dto.request.CreateRestaurantRequestDto;
import com.gastropolis.plazoleta.domain.model.RestaurantModel;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-31T16:12:53-0500",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.4.jar, environment: Java 17.0.9 (Amazon.com Inc.)"
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
        restaurantModel.setOwnerDni( dto.getOwnerDni() );

        return restaurantModel;
    }
}
