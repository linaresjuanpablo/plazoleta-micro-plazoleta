package com.gastropolis.plazoleta.infrastructure.out.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "restaurants")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 150, nullable = false)
    private String name;

    @Column(name = "nit", length = 20, unique = true, nullable = false)
    private String nit;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "phone", length = 13, nullable = false)
    private String phone;

    @Column(name = "logo_url")
    private String logoUrl;

    @Column(name = "owner_id", nullable = false)
    private Long ownerId;
}
