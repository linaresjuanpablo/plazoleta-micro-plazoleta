package com.gastropolis.plazoleta.infrastructure.out.jpa.mapper;

import com.gastropolis.plazoleta.domain.model.DishModel;
import com.gastropolis.plazoleta.infrastructure.out.jpa.entity.DishEntity;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-30T17:35:04-0500",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.4.jar, environment: Java 17.0.9 (Amazon.com Inc.)"
)
@Component
public class IDishEntityMapperImpl implements IDishEntityMapper {

    @Override
    public DishEntity toEntity(DishModel model) {
        if ( model == null ) {
            return null;
        }

        DishEntity dishEntity = new DishEntity();

        dishEntity.setId( model.getId() );
        dishEntity.setName( model.getName() );
        dishEntity.setDescription( model.getDescription() );
        dishEntity.setPrice( model.getPrice() );
        dishEntity.setImageUrl( model.getImageUrl() );
        dishEntity.setActive( model.getActive() );
        dishEntity.setCategoryId( model.getCategoryId() );
        dishEntity.setRestaurantId( model.getRestaurantId() );

        return dishEntity;
    }

    @Override
    public DishModel toModel(DishEntity entity) {
        if ( entity == null ) {
            return null;
        }

        DishModel dishModel = new DishModel();

        dishModel.setId( entity.getId() );
        dishModel.setName( entity.getName() );
        dishModel.setDescription( entity.getDescription() );
        dishModel.setPrice( entity.getPrice() );
        dishModel.setImageUrl( entity.getImageUrl() );
        dishModel.setActive( entity.getActive() );
        dishModel.setCategoryId( entity.getCategoryId() );
        dishModel.setRestaurantId( entity.getRestaurantId() );

        return dishModel;
    }

    @Override
    public List<DishModel> toModelList(List<DishEntity> entities) {
        if ( entities == null ) {
            return null;
        }

        List<DishModel> list = new ArrayList<DishModel>( entities.size() );
        for ( DishEntity dishEntity : entities ) {
            list.add( toModel( dishEntity ) );
        }

        return list;
    }
}
