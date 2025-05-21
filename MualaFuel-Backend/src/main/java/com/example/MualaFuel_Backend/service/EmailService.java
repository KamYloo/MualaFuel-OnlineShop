package com.example.MualaFuel_Backend.service;

import com.example.MualaFuel_Backend.dto.request.ContactRequest;
import com.example.MualaFuel_Backend.entity.Order;
import jakarta.mail.MessagingException;

import java.sql.SQLException;

public interface EmailService {
    void sendOrderConfirmationEmail(Order order) throws MessagingException, SQLException;
    void sendContactFormEmail(ContactRequest contactFormRequest) throws MessagingException;
}
