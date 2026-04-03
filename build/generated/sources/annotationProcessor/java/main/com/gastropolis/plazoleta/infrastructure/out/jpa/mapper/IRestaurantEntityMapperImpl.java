package com.gastropolis.plazoleta.infrastructure.out.jpa.mapper;

import com.gastropolis.plazoleta.domain.model.RestaurantModel;
import com.gastropolis.plazoleta.infrastructure.out.jpa.entity.RestaurantEntity;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-31T16:12:53-0500",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.4.jar, environment: Java 17.0.9 (Amazon.com Inc.)"
)
@Component
public class IRestaurantEntityMapperImpl implements IRestaurantEntityMapper {

    @Override
    public RestaurantEntity toEntity(RestaurantModel model) {
        if ( model == null ) {
            return null;
        }

        RestaurantEntity restaurantEntity = new RestaurantEntity();

        restaurantEntity.setId( model.getId() );
        restaurantEntity.setName( model.getName() );
        restaurantEntity.setNit( model.getNit() );
        restaurantEntity.setAddress( model.getAddress() );
        restaurantEntity.setPhone( model.getPhone() );
        restaurantEntity.setLogoUrl( model.getLogoUrl() );
        restaurantEntity.setOwnerId( model.getOwnerId() );

        return restaurantEntity;
    }

    @Override
    public RestaurantModel toModel(RestaurantEntity entity) {
        if ( entity == null ) {
            return null;
        }

        RestaurantModel restaurantModel = new RestaurantModel();

        restaurantModel.setId( entity.getId() );
        restaurantModel.setName( entity.getName() );
        restaurantModel.setNit( entity.getNit() );
        restaurantModel.setAddress( entity.getAddress() );
        restaurantModel.setPhone( entity.getPhone() );
        restaurantModel.setLogoUrl( entity.getLogoUrl() );
        restaurantModel.setOwnerId( entity.getOwnerId() );

        return restaurantModel;
    }

    @Override
    public List<RestaurantModel> toModelList(List<RestaurantEntity> entities) {
        if ( entities == null ) {
            return null;
        }

        List<RestaurantModel> list = new ArrayList<RestaurantModel>( entities.size() );
        for ( RestaurantEntity restaurantEntity : entities ) {
            list.add( toModel( restaurantEntity ) );
        }

        return list;
    }
}
