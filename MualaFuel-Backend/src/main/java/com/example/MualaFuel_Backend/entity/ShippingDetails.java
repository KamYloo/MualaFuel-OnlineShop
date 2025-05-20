package com.example.MualaFuel_Backend.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShippingDetails {
    private String shipping_country;
    private String shipping_city;
    private String shipping_zipCode;
    private String shipping_street;
}
