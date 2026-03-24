package com.gastropolis.plazoleta.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeliverOrderRequestDto {

    @NotBlank(message = "El PIN es obligatorio")
    private String pin;
}
