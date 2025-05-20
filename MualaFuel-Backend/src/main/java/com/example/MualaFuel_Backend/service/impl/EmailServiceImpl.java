package com.example.MualaFuel_Backend.service.impl;

import com.example.MualaFuel_Backend.email.EmailTemplateName;
import com.example.MualaFuel_Backend.entity.EmailHistory;
import com.example.MualaFuel_Backend.entity.Order;
import com.example.MualaFuel_Backend.service.EmailHistoryService;
import com.example.MualaFuel_Backend.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.context.Context;

import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;
    private final EmailHistoryService emailHistoryService;

    @Override
    public void sendOrderConfirmationEmail(Order order) throws MessagingException, SQLException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(
                message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name()
        );

        Context ctx = new Context();
        ctx.setVariable("order", order);
        ctx.setVariable("items", order.getOrderItems());
        ctx.setVariable("total", order.getTotalAmount());
        ctx.setVariable("userName", order.getUser().getName());

        String htmlBody = templateEngine.process(
                EmailTemplateName.ORDER_CONFIRMATION.getName(),
                ctx
        );

        String recipient = order.getUser().getEmail();
        String subject = "Potwierdzenie zamówienia #" + order.getId();

        helper.setTo(recipient);
        helper.setSubject(subject);
        helper.setText(htmlBody, true);
        helper.setFrom("no-reply@muala-fuel.com");

        mailSender.send(message);

        EmailHistory emailHistoryRecord = EmailHistory.builder()
                .recipient(recipient)
                .subject(subject)
                .body(htmlBody)
                .sentAt(LocalDateTime.now())
                .order(order)
                .build();

        emailHistoryService.saveEmailHistory(emailHistoryRecord);
    }
}
