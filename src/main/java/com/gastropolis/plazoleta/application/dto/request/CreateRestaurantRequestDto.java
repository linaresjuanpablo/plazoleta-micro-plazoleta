package com.gastropolis.plazoleta.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateRestaurantRequestDto {

    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    @NotBlank(message = "El NIT es obligatorio")
    @Pattern(regexp = "^[0-9]+$", message = "El NIT debe ser solo numérico")
    private String nit;

    @NotBlank(message = "La dirección es obligatoria")
    private String address;

    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(regexp = "^\\+?[0-9]{1,13}$", message = "El teléfono debe ser numérico y tener máximo 13 caracteres")
    private String phone;

    private String logoUrl;

    @NotBlank(message = "El DNI del propietario es obligatorio")
    @Pattern(regexp = "^[0-9]+$", message = "El DNI del propietario debe ser solo numérico")
    private String ownerDni;
}
