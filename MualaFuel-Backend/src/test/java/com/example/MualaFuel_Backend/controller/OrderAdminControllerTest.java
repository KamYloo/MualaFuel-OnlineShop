package com.example.MualaFuel_Backend.controller;

import com.example.MualaFuel_Backend.dto.OrderDto;
import com.example.MualaFuel_Backend.dto.OrderItemDto;
import com.example.MualaFuel_Backend.entity.PaymentDetails;
import com.example.MualaFuel_Backend.entity.ShippingDetails;
import com.example.MualaFuel_Backend.enums.OrderStatus;
import com.example.MualaFuel_Backend.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OrderAdminControllerTest {

    @Mock
    OrderService orderService;

    @InjectMocks
    OrderAdminController orderController;

    OrderDto sampleOrderDto;
    ShippingDetails shippingDetails;
    PaymentDetails paymentDetails;
    Principal principal;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        shippingDetails = ShippingDetails.builder()
                .shipping_street("Testowa 1")
                .shipping_city("Warszawa")
                .shipping_zipCode("00-000")
                .shipping_country("PL")
                .build();
        paymentDetails = PaymentDetails.builder()
                .payment_method("CARD")
                .payment_status("PAID")
                .payment_transactionId("TX123")
                .build();
        sampleOrderDto = new OrderDto(
                1L,
                BigDecimal.valueOf(100),
                OrderStatus.NEW,
                List.of(new OrderItemDto()),
                LocalDate.now(),
                shippingDetails,
                paymentDetails
        );
        principal = () -> "jan@kowalski.pl";
    }

    @Test
    void testGetAllOrders() {
        when(orderService.getAllOrders()).thenReturn(List.of(sampleOrderDto));

        ResponseEntity<List<?>> list = orderController.getAllOrders();

        assertEquals(200, list.getStatusCodeValue());
        assertFalse(list.getBody().isEmpty());
        assertEquals(sampleOrderDto, list.getBody().get(0));

        verify(orderService, times(1)).getAllOrders();
    }

    @Test
    void testUpdateOrderStatus() throws Exception {
        doNothing().when(orderService).updateStatusOfOrder(1L);

        ResponseEntity<String> response = orderController.updateOrderStatus(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Order status updated", response.getBody());
        verify(orderService).updateStatusOfOrder(1L);
    }

    @Test
    void testCancelOrder() throws Exception {
        doNothing().when(orderService).cancelOrder(1L);

        ResponseEntity<String> response = orderController.cancelOrder(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Order cancelled", response.getBody());
        verify(orderService).cancelOrder(1L);
    }

}
