package com.example.MualaFuel_Backend.controller;

import com.example.MualaFuel_Backend.dto.request.ContactRequest;
import com.example.MualaFuel_Backend.service.EmailService;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ContactControllerTest {

    @Mock
    EmailService emailService;

    @InjectMocks
    ContactController contactController;

    ContactRequest contactRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        contactRequest = new ContactRequest();
        contactRequest.setName("Jan");
        contactRequest.setEmail("jan@kowalski.pl");
        contactRequest.setSubject("Temat");
        contactRequest.setMessage("Wiadomość");
    }

    @Test
    void testSendContactFormSuccess() throws Exception {
        doNothing().when(emailService).sendContactFormEmail(contactRequest);

        ResponseEntity<String> response = contactController.sendContactForm(contactRequest);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("The message has been sent", response.getBody());
        verify(emailService).sendContactFormEmail(contactRequest);
    }

    @Test
    void testSendContactFormThrowsMessagingException() throws Exception {
        doThrow(new MessagingException("fail")).when(emailService).sendContactFormEmail(contactRequest);

        assertThrows(MessagingException.class, () -> contactController.sendContactForm(contactRequest));
        verify(emailService).sendContactFormEmail(contactRequest);
    }
}
