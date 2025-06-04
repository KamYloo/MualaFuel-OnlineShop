package com.example.MualaFuel_Backend.dao;

import com.example.MualaFuel_Backend.entity.*;
import com.example.MualaFuel_Backend.entity.Order;
import com.example.MualaFuel_Backend.enums.OrderStatus;
import com.example.MualaFuel_Backend.factory.ConnectionFactory;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderDaoTest {

    @Mock Connection connection;
    @Mock PreparedStatement preparedStatement;
    @Mock ResultSet resultSet;
    @Mock OrderItemDao orderItemDao;
    @Mock UserDao userDao;

    @InjectMocks OrderDao orderDao;

    private MockedStatic<ConnectionFactory> connectionFactoryMock;

    Order sampleOrder;
    User sampleUser;
    ShippingDetails shippingDetails;
    PaymentDetails paymentDetails;
    OrderItem sampleOrderItem;

    @BeforeEach
    void setUp() throws Exception {
        sampleUser = User.builder().id(1L).email("test@user.com").build();
        shippingDetails = ShippingDetails.builder()
                .shipping_street("Street")
                .shipping_city("City")
                .shipping_zipCode("00-000")
                .shipping_country("PL")
                .build();
        paymentDetails = PaymentDetails.builder()
                .payment_method("CARD")
                .payment_status("PAID")
                .payment_transactionId("TX123")
                .build();
        sampleOrderItem = OrderItem.builder().id(10L).quantity(2).unitPrice(BigDecimal.valueOf(20)).build();
        sampleOrder = Order.builder()
                .id(5L)
                .totalAmount(BigDecimal.valueOf(40))
                .status(OrderStatus.NEW)
                .user(sampleUser)
                .orderItems(List.of(sampleOrderItem))
                .orderDate(LocalDate.now())
                .address(shippingDetails)
                .paymentDetails(paymentDetails)
                .build();

        connectionFactoryMock = Mockito.mockStatic(ConnectionFactory.class);
        connectionFactoryMock.when(ConnectionFactory::getConnection).thenReturn(connection);
    }

    @AfterEach
    void tearDown() {
        connectionFactoryMock.close();
    }

    @Test
    void testSaveNewOrder() throws Exception {
        Order newOrder = Order.builder()
                .totalAmount(BigDecimal.valueOf(100))
                .status(OrderStatus.NEW)
                .user(sampleUser)
                .orderDate(LocalDate.now())
                .address(shippingDetails)
                .paymentDetails(paymentDetails)
                .build();

        when(connection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);
        ResultSet generatedKeys = mock(ResultSet.class);
        when(preparedStatement.getGeneratedKeys()).thenReturn(generatedKeys);
        when(generatedKeys.next()).thenReturn(true);
        when(generatedKeys.getLong(1)).thenReturn(123L);

        Order result = orderDao.save(newOrder);

        assertNotNull(result);
        assertEquals(123L, result.getId());
        verify(preparedStatement).setBigDecimal(1, BigDecimal.valueOf(100));
        verify(preparedStatement).setString(2, "NEW");
        verify(preparedStatement).setLong(3, sampleUser.getId());
        verify(preparedStatement).executeUpdate();
    }

    @Test
    void testUpdateOrder() throws Exception {
        Order updateOrder = Order.builder()
                .id(5L)
                .totalAmount(BigDecimal.valueOf(200))
                .status(OrderStatus.PAID)
                .user(sampleUser)
                .orderDate(LocalDate.now())
                .address(shippingDetails)
                .paymentDetails(paymentDetails)
                .build();

        when(connection.prepareStatement(startsWith("UPDATE orders"))).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        Order result = orderDao.save(updateOrder);

        assertNotNull(result);
        assertEquals(5L, result.getId());
        verify(preparedStatement).setBigDecimal(1, BigDecimal.valueOf(200));
        verify(preparedStatement).setString(2, "PAID");
        verify(preparedStatement).setLong(3, sampleUser.getId());
        verify(preparedStatement).setLong(12, 5L);
        verify(preparedStatement).executeUpdate();
    }

    @Test
    void testFindByIdReturnsOrder() throws Exception {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getLong("id")).thenReturn(5L);
        when(resultSet.getBigDecimal("total_amount")).thenReturn(BigDecimal.valueOf(40));
        when(resultSet.getString("status")).thenReturn("NEW");
        when(resultSet.getLong("user_id")).thenReturn(1L);
        when(resultSet.getDate("order_date")).thenReturn(Date.valueOf(LocalDate.now()));
        when(resultSet.getString("shipping_street")).thenReturn("Street");
        when(resultSet.getString("shipping_city")).thenReturn("City");
        when(resultSet.getString("shipping_zip_code")).thenReturn("00-000");
        when(resultSet.getString("shipping_country")).thenReturn("PL");
        when(resultSet.getString("payment_method")).thenReturn("CARD");
        when(resultSet.getString("payment_status")).thenReturn("PAID");
        when(resultSet.getString("payment_transaction_id")).thenReturn("TX123");

        when(orderItemDao.findByOrderId(5L)).thenReturn(List.of(sampleOrderItem));
        when(userDao.findById(1L)).thenReturn(Optional.of(sampleUser));

        Optional<Order> result = orderDao.findById(5L);

        assertTrue(result.isPresent());
        assertEquals(5L, result.get().getId());
        assertEquals("Street", result.get().getAddress().getShipping_street());
        assertEquals(1, result.get().getOrderItems().size());
        verify(preparedStatement).setLong(1, 5L);
        verify(preparedStatement).executeQuery();
    }

    @Test
    void testFindByIdReturnsEmpty() throws Exception {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        Optional<Order> result = orderDao.findById(999L);

        assertFalse(result.isPresent());
    }

    @Test
    void testFindByUserIdReturnsOrders() throws Exception {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getLong("id")).thenReturn(5L);
        when(resultSet.getBigDecimal("total_amount")).thenReturn(BigDecimal.valueOf(40));
        when(resultSet.getString("status")).thenReturn("NEW");
        when(resultSet.getLong("user_id")).thenReturn(1L);
        when(resultSet.getDate("order_date")).thenReturn(Date.valueOf(LocalDate.now()));
        when(resultSet.getString("shipping_street")).thenReturn("Street");
        when(resultSet.getString("shipping_city")).thenReturn("City");
        when(resultSet.getString("shipping_zip_code")).thenReturn("00-000");
        when(resultSet.getString("shipping_country")).thenReturn("PL");
        when(resultSet.getString("payment_method")).thenReturn("CARD");
        when(resultSet.getString("payment_status")).thenReturn("PAID");
        when(resultSet.getString("payment_transaction_id")).thenReturn("TX123");

        when(orderItemDao.findByOrderId(5L)).thenReturn(List.of(sampleOrderItem));
        when(userDao.findById(1L)).thenReturn(Optional.of(sampleUser));

        List<Order> orders = orderDao.findByUserId(1L);

        assertEquals(1, orders.size());
        assertEquals(5L, orders.get(0).getId());
        assertEquals("Street", orders.get(0).getAddress().getShipping_street());
        verify(preparedStatement).setLong(1, 1L);
        verify(preparedStatement).executeQuery();
    }

    @Test
    void testFindAllReturnsOrders() throws Exception {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getLong("id")).thenReturn(5L);
        when(resultSet.getBigDecimal("total_amount")).thenReturn(BigDecimal.valueOf(40));
        when(resultSet.getString("status")).thenReturn("NEW");
        when(resultSet.getLong("user_id")).thenReturn(1L);
        when(resultSet.getDate("order_date")).thenReturn(Date.valueOf(LocalDate.now()));
        when(resultSet.getString("shipping_street")).thenReturn("Street");
        when(resultSet.getString("shipping_city")).thenReturn("City");
        when(resultSet.getString("shipping_zip_code")).thenReturn("00-000");
        when(resultSet.getString("shipping_country")).thenReturn("PL");
        when(resultSet.getString("payment_method")).thenReturn("CARD");
        when(resultSet.getString("payment_status")).thenReturn("PAID");
        when(resultSet.getString("payment_transaction_id")).thenReturn("TX123");

        when(orderItemDao.findByOrderId(5L)).thenReturn(List.of(sampleOrderItem));
        when(userDao.findById(1L)).thenReturn(Optional.of(sampleUser));

        List<Order> orders = orderDao.findAll();

        assertEquals(1, orders.size());
        assertEquals(5L, orders.get(0).getId());
        verify(preparedStatement).executeQuery();
    }

    @Test
    void testDeleteOrder() throws Exception {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        assertDoesNotThrow(() -> orderDao.delete(5));
        verify(preparedStatement).setInt(1, 5);
        verify(preparedStatement).executeUpdate();
    }
}
