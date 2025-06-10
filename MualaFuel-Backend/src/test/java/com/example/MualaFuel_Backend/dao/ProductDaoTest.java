package com.example.MualaFuel_Backend.dao;

import com.example.MualaFuel_Backend.entity.Product;
import com.example.MualaFuel_Backend.enums.AlcoholType;
import com.example.MualaFuel_Backend.factory.ConnectionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.sql.*;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductDaoTest {

    @Mock Connection connection;
    @Mock PreparedStatement preparedStatement;
    @Mock ResultSet resultSet;

    @InjectMocks
    ProductDaoImpl productDao;

    Product sampleProduct;

    private MockedStatic<ConnectionFactory> connectionFactoryMock;

    @BeforeEach
    void setUp() throws Exception {
        sampleProduct = Product.builder()
                .id(1L)
                .name("Test Product")
                .description("desc")
                .price(BigDecimal.valueOf(10.99))
                .brand("BrandX")
                .alcoholType(AlcoholType.WINE)
                .quantity(5)
                .alcoholContent(12.5)
                .capacityInMilliliters(750)
                .imagePath("img.jpg")
                .build();

        connectionFactoryMock = Mockito.mockStatic(ConnectionFactory.class);
        connectionFactoryMock.when(ConnectionFactory::getConnection).thenReturn(connection);
    }

    @AfterEach
    void tearDown() {
        if (connectionFactoryMock != null) {
            connectionFactoryMock.close();
        }
    }

    @Test
    void testSaveProduct() throws Exception {
        when(connection.prepareStatement(anyString(), anyInt())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);
        ResultSet generatedKeys = mock(ResultSet.class);
        when(preparedStatement.getGeneratedKeys()).thenReturn(generatedKeys);
        when(generatedKeys.next()).thenReturn(true);
        when(generatedKeys.getLong(1)).thenReturn(1L);

        Product result = productDao.save(sampleProduct);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(connection).setAutoCommit(false);
        verify(preparedStatement).executeUpdate();
        verify(connection).commit();
    }

    @Test
    void testFindByIdReturnsProduct() throws Exception {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getLong("id")).thenReturn(1L);
        when(resultSet.getString("name")).thenReturn("Test Product");
        when(resultSet.getString("description")).thenReturn("desc");
        when(resultSet.getBigDecimal("price")).thenReturn(BigDecimal.valueOf(10.99));
        when(resultSet.getString("brand")).thenReturn("BrandX");
        when(resultSet.getString("alcohol_type")).thenReturn("WINE");
        when(resultSet.getInt("quantity")).thenReturn(5);
        when(resultSet.getDouble("alcohol_content")).thenReturn(12.5);
        when(resultSet.getInt("capacity_in_milliliters")).thenReturn(750);
        when(resultSet.getString("image_path")).thenReturn("img.jpg");

        Optional<Product> result = productDao.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("Test Product", result.get().getName());
        verify(preparedStatement).setLong(1, 1L);
        verify(preparedStatement).executeQuery();
    }

    @Test
    void testFindByIdReturnsEmpty() throws Exception {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        Optional<Product> result = productDao.findById(999L);

        assertFalse(result.isPresent());
    }

    @Test
    void testUpdateProduct() throws Exception {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        Product updated = productDao.update(sampleProduct);

        assertNotNull(updated);
        verify(preparedStatement).executeUpdate();
    }

    @Test
    void testDeleteProduct() throws Exception {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        assertDoesNotThrow(() -> productDao.delete(1L));
        verify(preparedStatement).setLong(1, 1L);
        verify(preparedStatement).executeUpdate();
    }

    @Test
    void testSaveThrowsException() throws Exception {
        when(connection.prepareStatement(anyString(), anyInt())).thenThrow(new SQLException("DB error"));

        assertThrows(RuntimeException.class, () -> productDao.save(sampleProduct));
    }
}
