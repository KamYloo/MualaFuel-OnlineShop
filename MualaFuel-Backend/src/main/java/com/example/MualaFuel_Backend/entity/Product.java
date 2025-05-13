package com.example.MualaFuel_Backend.entity;

import com.example.MualaFuel_Backend.enums.AlcoholType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity(name = "product")
@Table(name = "product")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(nullable = false)
    private String name;

    @Size(max = 1000)
    private String description;

    @NotNull
    @Positive
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Size(max = 100)
    @Column(nullable = false)
    private String brand;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AlcoholType alcoholType;

    @PositiveOrZero
    @Column(nullable = false)
    private Integer quantity;

    @Positive
    @Column(nullable = false)
    private Double alcoholContent;

    @Positive
    @Column(nullable = false)
    private Integer capacityInMilliliters;

    private String imagePath;
}
