package com.gastropolis.plazoleta.application.mapper;

import com.gastropolis.plazoleta.application.dto.response.DishResponseDto;
import com.gastropolis.plazoleta.domain.model.DishModel;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-30T17:35:03-0500",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.4.jar, environment: Java 17.0.9 (Amazon.com Inc.)"
)
@Component
public class IDishResponseMapperImpl implements IDishResponseMapper {

    @Override
    public DishResponseDto toDishResponseDto(DishModel model) {
        if ( model == null ) {
            return null;
        }

        DishResponseDto dishResponseDto = new DishResponseDto();

        dishResponseDto.setId( model.getId() );
        dishResponseDto.setName( model.getName() );
        dishResponseDto.setDescription( model.getDescription() );
        dishResponseDto.setPrice( model.getPrice() );
        dishResponseDto.setImageUrl( model.getImageUrl() );
        dishResponseDto.setActive( model.getActive() );

        return dishResponseDto;
    }

    @Override
    public List<DishResponseDto> toDishResponseDtoList(List<DishModel> models) {
        if ( models == null ) {
            return null;
        }

        List<DishResponseDto> list = new ArrayList<DishResponseDto>( models.size() );
        for ( DishModel dishModel : models ) {
            list.add( toDishResponseDto( dishModel ) );
        }

        return list;
    }
}
