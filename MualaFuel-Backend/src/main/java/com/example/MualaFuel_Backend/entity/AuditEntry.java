package com.example.MualaFuel_Backend.entity;

import com.example.MualaFuel_Backend.enums.AuditLevel;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "audit_entry")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private AuditLevel level;

    @Column(nullable = false, length = 100)
    private String eventType;

    @Column(nullable = false)
    private String source;

    @Column(columnDefinition = "text")
    private String details;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
