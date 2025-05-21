package com.example.MualaFuel_Backend.controller;

import com.example.MualaFuel_Backend.dto.EmailHistoryDto;
import com.example.MualaFuel_Backend.dto.request.EmailFilterRequest;
import com.example.MualaFuel_Backend.service.EmailHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/emailHistory")
public class EmailHistoryController {

    private final EmailHistoryService emailHistoryService;

    @GetMapping("/all")
    public ResponseEntity<Page<EmailHistoryDto>> getEmails(Pageable pageable,
                                                           @ModelAttribute EmailFilterRequest filter) {
        Page<EmailHistoryDto> page = emailHistoryService.getEmailHistories(pageable, filter);
        return ResponseEntity.ok(page);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEmailHistory(@PathVariable Long id) {
        emailHistoryService.deleteEmailHistory(id);
        return ResponseEntity.ok(id);
    }
}
