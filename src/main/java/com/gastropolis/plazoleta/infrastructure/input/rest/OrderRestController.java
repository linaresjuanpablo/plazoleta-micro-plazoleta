package com.gastropolis.plazoleta.infrastructure.input.rest;

import com.gastropolis.plazoleta.application.dto.request.CreateOrderRequestDto;
import com.gastropolis.plazoleta.application.dto.request.DeliverOrderRequestDto;
import com.gastropolis.plazoleta.application.dto.response.ApiResponse;
import com.gastropolis.plazoleta.application.dto.response.OrderResponseDto;
import com.gastropolis.plazoleta.application.handler.IOrderHandler;
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
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
@Tag(name = "Pedido", description = "Endpoints para gestión de pedidos")
@SecurityRequirement(name = "bearerAuth")
public class OrderRestController {

    private final IOrderHandler orderHandler;

    @PostMapping("/")
    @PreAuthorize("hasAuthority('CLIENTE')")
    @Operation(summary = "Crear pedido", description = "HU11 - Crea un nuevo pedido. Solo CLIENTE.")
    public ResponseEntity<ApiResponse<OrderResponseDto>> createOrder(
            @Valid @RequestBody CreateOrderRequestDto dto,
            @RequestHeader("Authorization") String token) {
        Long clientId = getAuthenticatedUserId();
        OrderResponseDto data = orderHandler.createOrder(dto, clientId, token);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("Pedido creado exitosamente", data));
    }

    @GetMapping("/")
    @PreAuthorize("hasAuthority('EMPLEADO')")
    @Operation(summary = "Listar pedidos", description = "HU12 - Lista pedidos del restaurante del empleado, con filtro por estado. Solo EMPLEADO.")
    public ResponseEntity<ApiResponse<Page<OrderResponseDto>>> listOrders(
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestHeader("Authorization") String token) {
        Long employeeId = getAuthenticatedUserId();
        Page<OrderResponseDto> data = orderHandler.listOrders(employeeId, status, page, size, token);
        return ResponseEntity.ok(new ApiResponse<>("Pedidos obtenidos exitosamente", data));
    }

    @PatchMapping("/{id}/assign")
    @PreAuthorize("hasAuthority('EMPLEADO')")
    @Operation(summary = "Asignar pedido", description = "HU13 - El empleado se asigna al pedido. PENDIENTE → EN_PREPARACION. Solo EMPLEADO.")
    public ResponseEntity<ApiResponse<OrderResponseDto>> assignOrder(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token) {
        Long employeeId = getAuthenticatedUserId();
        OrderResponseDto data = orderHandler.assignOrder(id, employeeId, token);
        return ResponseEntity.ok(new ApiResponse<>("Pedido asignado exitosamente", data));
    }

    @PatchMapping("/{id}/ready")
    @PreAuthorize("hasAuthority('EMPLEADO')")
    @Operation(summary = "Marcar pedido listo", description = "HU14 - Marca el pedido como LISTO, genera PIN y envía SMS. Solo EMPLEADO.")
    public ResponseEntity<ApiResponse<OrderResponseDto>> markOrderReady(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token) {
        Long employeeId = getAuthenticatedUserId();
        OrderResponseDto data = orderHandler.markOrderReady(id, employeeId, token);
        return ResponseEntity.ok(new ApiResponse<>("Pedido marcado como listo exitosamente", data));
    }

    @PatchMapping("/{id}/deliver")
    @PreAuthorize("hasAuthority('EMPLEADO')")
    @Operation(summary = "Entregar pedido", description = "HU15 - Marca el pedido como ENTREGADO. Valida PIN. Solo EMPLEADO.")
    public ResponseEntity<ApiResponse<OrderResponseDto>> deliverOrder(
            @PathVariable Long id,
            @Valid @RequestBody DeliverOrderRequestDto dto,
            @RequestHeader("Authorization") String token) {
        Long employeeId = getAuthenticatedUserId();
        OrderResponseDto data = orderHandler.deliverOrder(id, employeeId, dto, token);
        return ResponseEntity.ok(new ApiResponse<>("Pedido entregado exitosamente", data));
    }

    @PatchMapping("/{id}/cancel")
    @PreAuthorize("hasAuthority('CLIENTE')")
    @Operation(summary = "Cancelar pedido", description = "HU16 - Cancela un pedido PENDIENTE. Solo CLIENTE.")
    public ResponseEntity<ApiResponse<OrderResponseDto>> cancelOrder(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token) {
        Long clientId = getAuthenticatedUserId();
        OrderResponseDto data = orderHandler.cancelOrder(id, clientId, token);
        return ResponseEntity.ok(new ApiResponse<>("Pedido cancelado exitosamente", data));
    }

    private Long getAuthenticatedUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (Long) auth.getDetails();
    }
}
