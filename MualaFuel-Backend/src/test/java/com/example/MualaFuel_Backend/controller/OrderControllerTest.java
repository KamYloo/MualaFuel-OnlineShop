package com.example.MualaFuel_Backend.controller;

import com.example.MualaFuel_Backend.dto.OrderDto;
import com.example.MualaFuel_Backend.dto.OrderItemDto;
import com.example.MualaFuel_Backend.dto.OrderRequest;
import com.example.MualaFuel_Backend.entity.PaymentDetails;
import com.example.MualaFuel_Backend.entity.ShippingDetails;
import com.example.MualaFuel_Backend.enums.OrderStatus;
import com.example.MualaFuel_Backend.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderControllerTest {

    @Mock
    OrderService orderService;

    @InjectMocks
    OrderController orderController;

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
    void testPlaceOrder() throws Exception {
        OrderRequest request = new OrderRequest(shippingDetails, paymentDetails);
        when(orderService.placeOrder(shippingDetails, paymentDetails, principal)).thenReturn(sampleOrderDto);

        ResponseEntity<OrderDto> response = orderController.placeOrder(request, principal);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(sampleOrderDto, response.getBody());
        verify(orderService).placeOrder(shippingDetails, paymentDetails, principal);
    }

    @Test
    void testGetOrdersOfUser() throws Exception {
        List<OrderDto> orders = List.of(sampleOrderDto);
        when(orderService.getAllOrdersOfUser(principal)).thenReturn(orders);

        ResponseEntity<List<OrderDto>> response = orderController.getOrdersOfUser(principal);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(orders, response.getBody());
        verify(orderService).getAllOrdersOfUser(principal);
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
