package com.example.MualaFuel_Backend.dto;

import com.example.MualaFuel_Backend.entity.PaymentDetails;
import com.example.MualaFuel_Backend.entity.ShippingDetails;

public record OrderRequest(
        ShippingDetails shippingDetails,
        PaymentDetails paymentDetails
) {}