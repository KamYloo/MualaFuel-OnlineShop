package com.example.MualaFuel_Backend.service;

import com.example.MualaFuel_Backend.dto.request.ContactRequest;
import com.example.MualaFuel_Backend.email.EmailTemplateName;
import com.example.MualaFuel_Backend.entity.EmailHistory;
import com.example.MualaFuel_Backend.entity.Order;
import com.example.MualaFuel_Backend.entity.OrderItem;
import com.example.MualaFuel_Backend.entity.User;
import com.example.MualaFuel_Backend.service.impl.EmailServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class EmailServiceTest {

    @Mock JavaMailSender mailSender;
    @Mock SpringTemplateEngine templateEngine;
    @Mock EmailHistoryService emailHistoryService;
    @Mock MimeMessage mimeMessage;

    @InjectMocks EmailServiceImpl emailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
    }

    @Test
    void testSendOrderConfirmationEmail() throws Exception {
        User user = User.builder()
                .firstName("Jan")
                .lastName("Kowalski")
                .email("jan@kowalski.pl")
                .build();
        OrderItem item = OrderItem.builder().quantity(2).unitPrice(BigDecimal.TEN).build();
        Order order = Order.builder()
                .id(123L)
                .user(user)
                .orderItems(List.of(item))
                .totalAmount(BigDecimal.valueOf(20))
                .orderDate(LocalDate.now())
                .build();

        String htmlBody = "<html>Order</html>";
        when(templateEngine.process(eq(EmailTemplateName.ORDER_CONFIRMATION.getName()), any(Context.class)))
                .thenReturn(htmlBody);

        emailService.sendOrderConfirmationEmail(order);

        verify(mailSender).send(mimeMessage);
        verify(emailHistoryService).saveEmailHistory(argThat(eh ->
                eh.getRecipient().equals("jan@kowalski.pl") &&
                eh.getSubject().contains("Potwierdzenie zamówienia") &&
                eh.getBody().equals(htmlBody) &&
                eh.getOrder().equals(order)
        ));
    }

    @Test
    void testSendContactFormEmail() throws Exception {
        ContactRequest contactRequest = new ContactRequest();
        contactRequest.setName("Anna");
        contactRequest.setEmail("anna@example.com");
        contactRequest.setSubject("Zapytanie");
        contactRequest.setMessage("Treść wiadomości");

        String htmlBody = "<html>Contact</html>";
        when(templateEngine.process(eq(EmailTemplateName.CONTACT_FORM_EMAIL.getName()), any(Context.class)))
                .thenReturn(htmlBody);

        emailService.sendContactFormEmail(contactRequest);

        verify(mailSender).send(mimeMessage);
        verify(emailHistoryService).saveEmailHistory(argThat(eh ->
                eh.getRecipient().equals("kontakt@muala-fuel.com") &&
                eh.getSubject().equals("Zapytanie") &&
                eh.getBody().equals(htmlBody)
        ));
    }

    @Test
    void testSendOrderConfirmationEmailThrowsMessagingException() throws Exception {
        Order order = Order.builder()
                .id(1L)
                .user(User.builder().firstName("A").lastName("B").email("a@b.com").build())
                .orderItems(List.of())
                .totalAmount(BigDecimal.ONE)
                .orderDate(LocalDate.now())
                .build();

        when(templateEngine.process(anyString(), any(Context.class))).thenReturn("body");
        doThrow(new RuntimeException("fail")).when(mailSender).send(any(MimeMessage.class));

        assertThrows(RuntimeException.class, () -> emailService.sendOrderConfirmationEmail(order));
    }

    @Test
    void testSendContactFormEmailThrowsMessagingException() throws Exception {
        ContactRequest contactRequest = new ContactRequest();
        contactRequest.setName("Anna");
        contactRequest.setEmail("anna@example.com");
        contactRequest.setSubject("Zapytanie");
        contactRequest.setMessage("Treść wiadomości");

        when(templateEngine.process(anyString(), any(Context.class))).thenReturn("body");
        doThrow(new RuntimeException("fail")).when(mailSender).send(any(MimeMessage.class));

        assertThrows(RuntimeException.class, () -> emailService.sendContactFormEmail(contactRequest));
    }
}
