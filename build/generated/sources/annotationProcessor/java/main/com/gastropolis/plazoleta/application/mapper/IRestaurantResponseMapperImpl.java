package com.gastropolis.plazoleta.application.mapper;

import com.gastropolis.plazoleta.application.dto.response.RestaurantResponseDto;
import com.gastropolis.plazoleta.domain.model.RestaurantModel;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-31T16:12:54-0500",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.4.jar, environment: Java 17.0.9 (Amazon.com Inc.)"
)
@Component
public class IRestaurantResponseMapperImpl implements IRestaurantResponseMapper {

    @Override
    public RestaurantResponseDto toRestaurantResponseDto(RestaurantModel model) {
        if ( model == null ) {
            return null;
        }

        RestaurantResponseDto restaurantResponseDto = new RestaurantResponseDto();

        restaurantResponseDto.setName( model.getName() );
        restaurantResponseDto.setLogoUrl( model.getLogoUrl() );

        return restaurantResponseDto;
    }

    @Override
    public List<RestaurantResponseDto> toRestaurantResponseDtoList(List<RestaurantModel> models) {
        if ( models == null ) {
            return null;
        }

        List<RestaurantResponseDto> list = new ArrayList<RestaurantResponseDto>( models.size() );
        for ( RestaurantModel restaurantModel : models ) {
            list.add( toRestaurantResponseDto( restaurantModel ) );
        }

        return list;
    }
}
