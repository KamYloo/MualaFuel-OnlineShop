package com.example.MualaFuel_Backend.email;

import lombok.Getter;

@Getter
public enum EmailTemplateName {
    ORDER_CONFIRMATION("order_confirmation");

    private final String name;

    EmailTemplateName(String name) {
        this.name = name;
    }
}
