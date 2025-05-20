package com.example.MualaFuel_Backend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailFilterRequest {
    private String recipient;
    private String subject;
    private LocalDateTime from;
    private LocalDateTime to;
    private Integer page;
    private Integer size;
}