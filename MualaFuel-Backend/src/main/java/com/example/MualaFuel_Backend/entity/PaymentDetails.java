package com.example.MualaFuel_Backend.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDetails {
    private String payment_method;
    private String payment_status;
    private String payment_transactionId;
}
