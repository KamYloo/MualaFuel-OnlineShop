package com.example.MualaFuel_Backend.dao;

import com.example.MualaFuel_Backend.entity.Order;
import com.example.MualaFuel_Backend.entity.OrderItem;
import com.example.MualaFuel_Backend.entity.Product;
import com.example.MualaFuel_Backend.factory.ConnectionFactory;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.sql.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderItemDaoTest {

    @Mock Connection connection;
    @Mock PreparedStatement preparedStatement;
    @Mock ResultSet resultSet;
    @Mock ProductDao productDao;

    @InjectMocks OrderItemDao orderItemDao;

    private MockedStatic<ConnectionFactory> connectionFactoryMock;

    OrderItem sampleOrderItem;
    Order sampleOrder;
    Product sampleProduct;

    @BeforeEach
    void setUp() throws Exception {
        sampleOrder = Order.builder().id(1L).build();
        sampleProduct = Product.builder().id(2L).build();
        sampleOrderItem = OrderItem.builder()
                .id(3L)
                .order(sampleOrder)
                .product(sampleProduct)
                .quantity(5)
                .unitPrice(BigDecimal.valueOf(10.99))
                .build();

        connectionFactoryMock = Mockito.mockStatic(ConnectionFactory.class);
        connectionFactoryMock.when(ConnectionFactory::getConnection).thenReturn(connection);
    }

    @AfterEach
    void tearDown() {
        connectionFactoryMock.close();
    }

    @Test
    void testSaveNewOrderItem() throws Exception {
        OrderItem newItem = OrderItem.builder()
                .order(sampleOrder)
                .product(sampleProduct)
                .quantity(2)
                .unitPrice(BigDecimal.valueOf(5.55))
                .build();

        when(connection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);
        ResultSet generatedKeys = mock(ResultSet.class);
        when(preparedStatement.getGeneratedKeys()).thenReturn(generatedKeys);
        when(generatedKeys.next()).thenReturn(true);
        when(generatedKeys.getLong(1)).thenReturn(99L);

        OrderItem result = orderItemDao.save(newItem);

        assertNotNull(result);
        assertEquals(99L, result.getId());
        verify(preparedStatement).setLong(1, 1L);
        verify(preparedStatement).setLong(2, 2L);
        verify(preparedStatement).setInt(3, 2);
        verify(preparedStatement).setBigDecimal(4, BigDecimal.valueOf(5.55));
        verify(preparedStatement).executeUpdate();
    }

    @Test
    void testUpdateOrderItem() throws Exception {
        when(connection.prepareStatement(startsWith("UPDATE order_item"))).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        OrderItem updated = orderItemDao.save(sampleOrderItem);

        assertNotNull(updated);
        verify(preparedStatement).setLong(1, 1L);
        verify(preparedStatement).setLong(2, 2L);
        verify(preparedStatement).setInt(3, 5);
        verify(preparedStatement).setBigDecimal(4, BigDecimal.valueOf(10.99));
        verify(preparedStatement).setLong(5, 3L);
        verify(preparedStatement).executeUpdate();
    }

    @Test
    void testFindByOrderId() throws Exception {
        when(connection.prepareStatement(startsWith("SELECT * FROM order_item"))).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getLong("id")).thenReturn(3L);
        when(resultSet.getInt("quantity")).thenReturn(5);
        when(resultSet.getBigDecimal("unit_price")).thenReturn(BigDecimal.valueOf(10.99));
        when(resultSet.getLong("order_id")).thenReturn(1L);
        when(resultSet.getLong("product_id")).thenReturn(2L);

        Product foundProduct = Product.builder().id(2L).build();
        when(productDao.findById(2L)).thenReturn(Optional.of(foundProduct));

        List<OrderItem> items = orderItemDao.findByOrderId(1L);

        assertEquals(1, items.size());
        assertEquals(3L, items.get(0).getId());
        assertEquals(2L, items.get(0).getProduct().getId());
        verify(preparedStatement).setLong(1, 1L);
        verify(preparedStatement).executeQuery();
    }

    @Test
    void testFindByIdReturnsOrderItem() throws Exception {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getLong("id")).thenReturn(3L);
        when(resultSet.getInt("quantity")).thenReturn(5);
        when(resultSet.getBigDecimal("unit_price")).thenReturn(BigDecimal.valueOf(10.99));
        when(resultSet.getLong("order_id")).thenReturn(1L);
        when(resultSet.getLong("product_id")).thenReturn(2L);

        Optional<OrderItem> result = orderItemDao.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(3L, result.get().getId());
        assertEquals(5, result.get().getQuantity());
    }

    @Test
    void testFindByIdReturnsEmpty() throws Exception {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        Optional<OrderItem> result = orderItemDao.findById(123L);

        assertFalse(result.isPresent());
    }

    @Test
    void testDeleteById() throws Exception {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        assertDoesNotThrow(() -> orderItemDao.deleteById(3L));
        verify(preparedStatement).setLong(1, 3L);
        verify(preparedStatement).executeUpdate();
    }
}
