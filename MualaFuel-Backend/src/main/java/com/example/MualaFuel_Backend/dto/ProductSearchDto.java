package com.example.MualaFuel_Backend.dto;

import com.example.MualaFuel_Backend.enums.AlcoholType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductSearchDto {
    private String name;
    private String brand;
    private List<AlcoholType> alcoholType;
    @NotNull(message = "Minimal price is required")
    private BigDecimal minPrice;
    @NotNull(message = "Maximal price is required")
    private BigDecimal maxPrice;
    @NotNull(message = "Minimal alcohol content is required")
    private Double minAlcoholContent;
    @NotNull(message = "Maximal alcohol content is required")
    private Double maxAlcoholContent;
    @NotNull(message = "Minimal capacity is required")
    private Integer minCapacity;
    @NotNull(message = "Maximal capacity is required")
    private Integer maxCapacity;
}
