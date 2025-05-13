package com.example.MualaFuel_Backend.dto;

import com.example.MualaFuel_Backend.enums.AlcoholType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDto {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String brand;
    private AlcoholType alcoholType;
    private Integer quantity;
    private Double alcoholContent;
    private Integer capacityInMilliliters;
    private String imagePath;
}
