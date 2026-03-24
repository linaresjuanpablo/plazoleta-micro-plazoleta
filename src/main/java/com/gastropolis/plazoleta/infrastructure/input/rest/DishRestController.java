package com.gastropolis.plazoleta.infrastructure.input.rest;

import com.gastropolis.plazoleta.application.dto.request.CreateDishRequestDto;
import com.gastropolis.plazoleta.application.dto.request.UpdateDishRequestDto;
import com.gastropolis.plazoleta.application.dto.response.ApiResponse;
import com.gastropolis.plazoleta.application.dto.response.DishResponseDto;
import com.gastropolis.plazoleta.application.handler.IDishHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/dish")
@RequiredArgsConstructor
@Tag(name = "Plato", description = "Endpoints para gestión de platos")
@SecurityRequirement(name = "bearerAuth")
public class DishRestController {

    private final IDishHandler dishHandler;

    @PostMapping("/")
    @PreAuthorize("hasAuthority('PROPIETARIO')")
    @Operation(summary = "Crear plato", description = "HU3 - Crea un nuevo plato. Solo PROPIETARIO del restaurante.")
    public ResponseEntity<ApiResponse<DishResponseDto>> createDish(@Valid @RequestBody CreateDishRequestDto dto) {
        Long userId = getAuthenticatedUserId();
        DishResponseDto data = dishHandler.createDish(dto, userId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("Plato creado exitosamente", data));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('PROPIETARIO')")
    @Operation(summary = "Modificar plato", description = "HU4 - Modifica precio y descripción del plato. Solo PROPIETARIO del restaurante.")
    public ResponseEntity<ApiResponse<DishResponseDto>> updateDish(@PathVariable Long id, @Valid @RequestBody UpdateDishRequestDto dto) {
        Long userId = getAuthenticatedUserId();
        DishResponseDto data = dishHandler.updateDish(id, dto, userId);
        return ResponseEntity.ok(new ApiResponse<>("Plato actualizado exitosamente", data));
    }

    @PatchMapping("/{id}/enable")
    @PreAuthorize("hasAuthority('PROPIETARIO')")
    @Operation(summary = "Habilitar/deshabilitar plato", description = "HU7 - Toggle del estado active del plato. Solo PROPIETARIO del restaurante.")
    public ResponseEntity<ApiResponse<DishResponseDto>> toggleDishActive(@PathVariable Long id) {
        Long userId = getAuthenticatedUserId();
        DishResponseDto data = dishHandler.toggleDishActive(id, userId);
        return ResponseEntity.ok(new ApiResponse<>("Estado del plato actualizado exitosamente", data));
    }

    @GetMapping("/")
    @PreAuthorize("hasAuthority('CLIENTE')")
    @Operation(summary = "Listar platos", description = "HU10 - Lista platos de un restaurante paginados con filtro opcional por categoría. Solo CLIENTE.")
    public ResponseEntity<ApiResponse<Page<DishResponseDto>>> listDishes(
            @RequestParam Long restaurantId,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<DishResponseDto> data = dishHandler.listDishesByRestaurant(restaurantId, categoryId, page, size);
        return ResponseEntity.ok(new ApiResponse<>("Platos obtenidos exitosamente", data));
    }

    private Long getAuthenticatedUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (Long) auth.getDetails();
    }
}
