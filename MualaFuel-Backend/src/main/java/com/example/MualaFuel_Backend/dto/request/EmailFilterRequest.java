package com.example.MualaFuel_Backend.dto.request;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class EmailFilterRequest {
    private String recipient;
    private String subject;
    private LocalDateTime from;
    private LocalDateTime to;
    private Boolean relatedToOrder;
    private Integer page;
    private Integer size;
}