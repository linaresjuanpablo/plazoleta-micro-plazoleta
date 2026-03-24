package com.gastropolis.plazoleta.infrastructure.configuration;

import com.gastropolis.plazoleta.domain.api.IDishServicePort;
import com.gastropolis.plazoleta.domain.api.IOrderServicePort;
import com.gastropolis.plazoleta.domain.api.IRestaurantServicePort;
import com.gastropolis.plazoleta.domain.spi.*;
import com.gastropolis.plazoleta.domain.usecase.DishUseCase;
import com.gastropolis.plazoleta.domain.usecase.OrderUseCase;
import com.gastropolis.plazoleta.domain.usecase.RestaurantUseCase;
import com.gastropolis.plazoleta.infrastructure.out.feign.MessagingFeignClient;
import com.gastropolis.plazoleta.infrastructure.out.feign.TraceabilityFeignClient;
import com.gastropolis.plazoleta.infrastructure.out.feign.UserFeignClient;
import com.gastropolis.plazoleta.infrastructure.out.feign.adapter.MessagingFeignAdapter;
import com.gastropolis.plazoleta.infrastructure.out.feign.adapter.TraceabilityFeignAdapter;
import com.gastropolis.plazoleta.infrastructure.out.feign.adapter.UserFeignAdapter;
import com.gastropolis.plazoleta.infrastructure.out.jpa.adapter.DishJpaAdapter;
import com.gastropolis.plazoleta.infrastructure.out.jpa.adapter.EmployeeRestaurantJpaAdapter;
import com.gastropolis.plazoleta.infrastructure.out.jpa.adapter.OrderJpaAdapter;
import com.gastropolis.plazoleta.infrastructure.out.jpa.adapter.RestaurantJpaAdapter;
import com.gastropolis.plazoleta.infrastructure.out.jpa.mapper.IDishEntityMapper;
import com.gastropolis.plazoleta.infrastructure.out.jpa.mapper.IOrderEntityMapper;
import com.gastropolis.plazoleta.infrastructure.out.jpa.mapper.IRestaurantEntityMapper;
import com.gastropolis.plazoleta.infrastructure.out.jpa.repository.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public IRestaurantPersistencePort restaurantPersistencePort(
            IRestaurantRepository restaurantRepository,
            IRestaurantEntityMapper restaurantEntityMapper) {
        return new RestaurantJpaAdapter(restaurantRepository, restaurantEntityMapper);
    }

    @Bean
    public IDishPersistencePort dishPersistencePort(
            IDishRepository dishRepository,
            IDishEntityMapper dishEntityMapper,
            ICategoryRepository categoryRepository,
            IRestaurantRepository restaurantRepository) {
        return new DishJpaAdapter(dishRepository, dishEntityMapper, categoryRepository, restaurantRepository);
    }

    @Bean
    public IOrderPersistencePort orderPersistencePort(
            IOrderRepository orderRepository,
            IOrderEntityMapper orderEntityMapper,
            IRestaurantRepository restaurantRepository,
            IDishRepository dishRepository,
            IEmployeeRestaurantRepository employeeRestaurantRepository) {
        return new OrderJpaAdapter(orderRepository, orderEntityMapper, restaurantRepository, dishRepository, employeeRestaurantRepository);
    }

    @Bean
    public IEmployeeRestaurantPersistencePort employeeRestaurantPersistencePort(
            IEmployeeRestaurantRepository employeeRestaurantRepository,
            IRestaurantRepository restaurantRepository) {
        return new EmployeeRestaurantJpaAdapter(employeeRestaurantRepository, restaurantRepository);
    }

    @Bean
    public IUserClientPort userClientPort(UserFeignClient userFeignClient) {
        return new UserFeignAdapter(userFeignClient);
    }

    @Bean
    public ITraceabilityClientPort traceabilityClientPort(TraceabilityFeignClient traceabilityFeignClient) {
        return new TraceabilityFeignAdapter(traceabilityFeignClient);
    }

    @Bean
    public IMessagingClientPort messagingClientPort(MessagingFeignClient messagingFeignClient) {
        return new MessagingFeignAdapter(messagingFeignClient);
    }

    @Bean
    public IRestaurantServicePort restaurantServicePort(
            IRestaurantPersistencePort restaurantPersistencePort,
            IUserClientPort userClientPort,
            IEmployeeRestaurantPersistencePort employeeRestaurantPersistencePort) {
        return new RestaurantUseCase(restaurantPersistencePort, userClientPort, employeeRestaurantPersistencePort);
    }

    @Bean
    public IDishServicePort dishServicePort(
            IDishPersistencePort dishPersistencePort,
            IRestaurantPersistencePort restaurantPersistencePort) {
        return new DishUseCase(dishPersistencePort, restaurantPersistencePort);
    }

    @Bean
    public IOrderServicePort orderServicePort(
            IOrderPersistencePort orderPersistencePort,
            IDishPersistencePort dishPersistencePort,
            IRestaurantPersistencePort restaurantPersistencePort,
            ITraceabilityClientPort traceabilityClientPort,
            IMessagingClientPort messagingClientPort,
            IUserClientPort userClientPort) {
        return new OrderUseCase(orderPersistencePort, dishPersistencePort, restaurantPersistencePort,
                traceabilityClientPort, messagingClientPort, userClientPort);
    }
}
