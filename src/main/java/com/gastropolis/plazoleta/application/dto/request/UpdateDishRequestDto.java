package com.gastropolis.plazoleta.application.dto.request;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateDishRequestDto {

    @Min(value = 1, message = "El precio debe ser mayor a 0")
    private Integer price;

    private String description;
}
