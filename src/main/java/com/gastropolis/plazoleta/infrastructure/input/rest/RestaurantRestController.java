package com.gastropolis.plazoleta.infrastructure.input.rest;

import com.gastropolis.plazoleta.application.dto.request.CreateEmployeeRequestDto;
import com.gastropolis.plazoleta.application.dto.request.CreateRestaurantRequestDto;
import com.gastropolis.plazoleta.application.dto.response.ApiResponse;
import com.gastropolis.plazoleta.application.dto.response.RestaurantResponseDto;
import com.gastropolis.plazoleta.application.handler.IRestaurantHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/restaurant")
@RequiredArgsConstructor
@Tag(name = "Restaurante", description = "Endpoints para gestión de restaurantes")
@SecurityRequirement(name = "bearerAuth")
public class RestaurantRestController {

    private final IRestaurantHandler restaurantHandler;

    @PostMapping("/")
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    @Operation(summary = "Crear restaurante", description = "HU2 - Crea un nuevo restaurante. Solo ADMINISTRADOR.")
    public ResponseEntity<ApiResponse<RestaurantResponseDto>> createRestaurant(
            @Valid @RequestBody CreateRestaurantRequestDto dto,
            @RequestHeader("Authorization") String token) {
        RestaurantResponseDto data = restaurantHandler.createRestaurant(dto, token);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("Restaurante creado exitosamente", data));
    }

    @GetMapping("/")
    @PreAuthorize("hasAuthority('CLIENTE')")
    @Operation(summary = "Listar restaurantes", description = "HU9 - Lista restaurantes paginados en orden alfabético. Solo CLIENTE.")
    public ResponseEntity<ApiResponse<Page<RestaurantResponseDto>>> listRestaurants(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<RestaurantResponseDto> data = restaurantHandler.listRestaurants(page, size);
        return ResponseEntity.ok(new ApiResponse<>("Restaurantes obtenidos exitosamente", data));
    }

    @PostMapping("/employee")
    @PreAuthorize("hasAuthority('PROPIETARIO')")
    @Operation(summary = "Crear empleado (HU6)", description = "HU6 - Crea un empleado y lo asigna al restaurante del propietario. Solo PROPIETARIO.")
    public ResponseEntity<ApiResponse<String>> createEmployee(
            @Valid @RequestBody CreateEmployeeRequestDto dto,
            @RequestHeader("Authorization") String token) {
        restaurantHandler.createEmployee(dto, token);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("Empleado creado y asignado al restaurante exitosamente", null));
    }
}
