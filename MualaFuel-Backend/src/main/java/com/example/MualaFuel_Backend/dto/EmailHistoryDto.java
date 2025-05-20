package com.example.MualaFuel_Backend.dto;

import com.example.MualaFuel_Backend.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailHistoryDto {
    private Long id;
    private String recipient;
    private String subject;
    private LocalDateTime sentAt;
    private Order order;
}
