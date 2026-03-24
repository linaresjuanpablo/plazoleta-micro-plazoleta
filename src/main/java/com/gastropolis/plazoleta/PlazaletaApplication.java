package com.gastropolis.plazoleta;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class PlazaletaApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlazaletaApplication.class, args);
    }
}
