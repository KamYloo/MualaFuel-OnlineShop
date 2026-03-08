package com.example.MualaFuel_Backend.service;

import com.example.MualaFuel_Backend.dao.OrderDaoImpl;
import com.example.MualaFuel_Backend.dao.OrderItemDaoImpl;
import com.example.MualaFuel_Backend.dao.ProductDaoImpl;
import com.example.MualaFuel_Backend.dao.UserDao;
import com.example.MualaFuel_Backend.dto.OrderDto;
import com.example.MualaFuel_Backend.entity.*;
import com.example.MualaFuel_Backend.enums.OrderStatus;
import com.example.MualaFuel_Backend.handler.BusinessErrorCodes;
import com.example.MualaFuel_Backend.handler.CustomException;
import com.example.MualaFuel_Backend.mapper.Mapper;
import com.example.MualaFuel_Backend.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @Mock Cart cart;
    @Mock
    OrderDaoImpl orderDao;
    @Mock
    ProductDaoImpl productDao;
    @Mock
    OrderItemDaoImpl orderItemDao;
    @Mock
    UserDao userDao;
    @Mock Mapper<Order, OrderDto> mapper;
    @Mock EmailService emailService;

    @InjectMocks OrderServiceImpl orderService;

    User sampleUser;
    Product sampleProduct;
    CartItem sampleCartItem;
    Order sampleOrder;
    OrderDto sampleOrderDto;
    ShippingDetails shippingDetails;
    PaymentDetails paymentDetails;
    Principal principal;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        sampleUser = User.builder()
                .id(1L)
                .email("jan@kowalski.pl")
                .firstName("Jan")
                .lastName("Kowalski")
                .build();

        sampleProduct = Product.builder()
                .id(2L)
                .name("Product")
                .price(BigDecimal.TEN)
                .quantity(10)
                .build();

        sampleCartItem = CartItem.builder()
                .productId(2L)
                .productName("Product")
                .price(BigDecimal.TEN)
                .quantity(2)
                .build();

        shippingDetails = ShippingDetails.builder()
                .shipping_country("PL")
                .shipping_city("Warszawa")
                .shipping_zipCode("00-000")
                .shipping_street("Testowa 1")
                .build();

        paymentDetails = PaymentDetails.builder()
                .payment_method("CARD")
                .payment_status("PAID")
                .payment_transactionId("TX123")
                .build();

        sampleOrder = Order.builder()
                .id(5L)
                .user(sampleUser)
                .orderDate(LocalDate.now())
                .status(OrderStatus.NEW)
                .address(shippingDetails)
                .paymentDetails(paymentDetails)
                .orderItems(new ArrayList<>())
                .totalAmount(BigDecimal.ZERO)
                .build();

        sampleOrderDto = new OrderDto(5L, BigDecimal.ZERO, OrderStatus.NEW, new ArrayList<>(), LocalDate.now(), shippingDetails, paymentDetails);

        principal = () -> "jan@kowalski.pl";
    }

    @Test
    void testPlaceOrderSuccess() throws Exception {
        when(cart.getItems()).thenReturn(List.of(sampleCartItem));
        when(userDao.findByEmail("jan@kowalski.pl")).thenReturn(Optional.of(sampleUser));
        when(orderDao.save(any(Order.class))).thenReturn(sampleOrder);
        when(productDao.findById(2L)).thenReturn(Optional.of(sampleProduct));
        when(productDao.update(any(Product.class))).thenReturn(sampleProduct);
        when(orderItemDao.save(any(OrderItem.class))).thenAnswer(inv -> inv.getArgument(0));
        when(mapper.mapTo(any(Order.class))).thenReturn(sampleOrderDto);

        OrderDto result = orderService.placeOrder(shippingDetails, paymentDetails, principal);

        assertNotNull(result);
        verify(cart).clear();
        verify(emailService).sendOrderConfirmationEmail(any(Order.class));
        verify(orderDao, atLeastOnce()).save(any(Order.class));
    }

    @Test
    void testPlaceOrderThrowsIfCartEmpty() {
        when(cart.getItems()).thenReturn(Collections.emptyList());

        Exception ex = assertThrows(RuntimeException.class,
                () -> orderService.placeOrder(shippingDetails, paymentDetails, principal));
        assertEquals("Cannot place empty order", ex.getMessage());
    }

    @Test
    void testPlaceOrderThrowsIfUserNotFound() {
        when(cart.getItems()).thenReturn(List.of(sampleCartItem));
        when(userDao.findByEmail("jan@kowalski.pl")).thenReturn(Optional.empty());

        CustomException ex = assertThrows(CustomException.class,
                () -> orderService.placeOrder(shippingDetails, paymentDetails, principal));
        assertEquals(BusinessErrorCodes.NOT_FOUND, ex.getErrorCode());
    }

    @Test
    void testPlaceOrderThrowsIfProductNotFound() throws SQLException {
        when(cart.getItems()).thenReturn(List.of(sampleCartItem));
        when(userDao.findByEmail("jan@kowalski.pl")).thenReturn(Optional.of(sampleUser));
        when(orderDao.save(any(Order.class))).thenReturn(sampleOrder);
        when(productDao.findById(2L)).thenReturn(Optional.empty());

        CustomException ex = assertThrows(CustomException.class,
                () -> orderService.placeOrder(shippingDetails, paymentDetails, principal));
        assertEquals(BusinessErrorCodes.NOT_FOUND, ex.getErrorCode());
    }

    @Test
    void testPlaceOrderThrowsIfInsufficientStock() throws Exception {
        Product lowStockProduct = Product.builder()
                .id(2L)
                .name("Product")
                .price(BigDecimal.TEN)
                .quantity(1)
                .build();

        when(cart.getItems()).thenReturn(List.of(sampleCartItem));
        when(userDao.findByEmail("jan@kowalski.pl")).thenReturn(Optional.of(sampleUser));
        when(orderDao.save(any(Order.class))).thenReturn(sampleOrder);
        when(productDao.findById(2L)).thenReturn(Optional.of(lowStockProduct));

        Exception ex = assertThrows(RuntimeException.class,
                () -> orderService.placeOrder(shippingDetails, paymentDetails, principal));
        assertTrue(ex.getMessage().contains("Insufficient stock"));
    }

    @Test
    void testGetAllOrdersOfUser() throws Exception {
        when(userDao.findByEmail("jan@kowalski.pl")).thenReturn(Optional.of(sampleUser));
        when(orderDao.findByUserId(1L)).thenReturn(List.of(sampleOrder));
        when(mapper.mapTo(sampleOrder)).thenReturn(sampleOrderDto);

        List<OrderDto> result = orderService.getAllOrdersOfUser(principal);

        assertEquals(1, result.size());
        assertEquals(sampleOrderDto, result.get(0));
    }

    @Test
    void testGetAllOrdersOfUserThrowsIfUserNotFound() {
        when(userDao.findByEmail("jan@kowalski.pl")).thenReturn(Optional.empty());

        CustomException ex = assertThrows(CustomException.class,
                () -> orderService.getAllOrdersOfUser(principal));
        assertEquals(BusinessErrorCodes.NOT_FOUND, ex.getErrorCode());
    }

    @Test
    void testUpdateStatusOfOrder() throws Exception {
        Order order = Order.builder().id(5L).status(OrderStatus.NEW).build();
        when(orderDao.findById(5L)).thenReturn(Optional.of(order));
        when(orderDao.save(any(Order.class))).thenReturn(order);

        orderService.updateStatusOfOrder(5L);

        assertEquals(OrderStatus.PAID, order.getStatus());
        verify(orderDao).save(order);
    }

    @Test
    void testUpdateStatusOfOrderThrowsIfOrderNotFound() throws Exception {
        when(orderDao.findById(5L)).thenReturn(Optional.empty());

        CustomException ex = assertThrows(CustomException.class,
                () -> orderService.updateStatusOfOrder(5L));
        assertEquals(BusinessErrorCodes.NOT_FOUND, ex.getErrorCode());
    }

    @Test
    void testCancelOrder() throws Exception {
        Order order = Order.builder().id(5L).status(OrderStatus.NEW).build();
        when(orderDao.findById(5L)).thenReturn(Optional.of(order));
        when(orderDao.save(any(Order.class))).thenReturn(order);

        orderService.cancelOrder(5L);

        assertEquals(OrderStatus.CANCELLED, order.getStatus());
        verify(orderDao).save(order);
    }

    @Test
    void testCancelOrderThrowsIfOrderNotFound() throws SQLException {
        when(orderDao.findById(5L)).thenReturn(Optional.empty());

        CustomException ex = assertThrows(CustomException.class,
                () -> orderService.cancelOrder(5L));
        assertEquals(BusinessErrorCodes.NOT_FOUND, ex.getErrorCode());
    }
}
