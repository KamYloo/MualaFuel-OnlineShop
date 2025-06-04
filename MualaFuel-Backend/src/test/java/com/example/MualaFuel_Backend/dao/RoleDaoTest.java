package com.example.MualaFuel_Backend.dao;

import com.example.MualaFuel_Backend.entity.Role;
import com.example.MualaFuel_Backend.factory.ConnectionFactory;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.*;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleDaoTest {

    @Mock Connection connection;
    @Mock PreparedStatement preparedStatement;
    @Mock ResultSet resultSet;

    @InjectMocks RoleDao roleDao;

    private MockedStatic<ConnectionFactory> connectionFactoryMock;

    Role sampleRole;

    @BeforeEach
    void setUp() throws Exception {
        sampleRole = Role.builder()
                .id(1L)
                .name("ADMIN")
                .build();

        connectionFactoryMock = Mockito.mockStatic(ConnectionFactory.class);
        connectionFactoryMock.when(ConnectionFactory::getConnection).thenReturn(connection);
    }

    @AfterEach
    void tearDown() {
        connectionFactoryMock.close();
    }

    @Test
    void testSaveRole() throws Exception {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        doNothing().when(preparedStatement).setLong(anyInt(), anyLong());
        doNothing().when(preparedStatement).setString(anyInt(), anyString());
        when(preparedStatement.executeUpdate()).thenReturn(1);

        assertDoesNotThrow(() -> roleDao.save(sampleRole));
        verify(preparedStatement).setLong(1, 1L);
        verify(preparedStatement).setString(2, "ADMIN");
        verify(preparedStatement).executeUpdate();
    }

    @Test
    void testFindByIdReturnsRole() throws Exception {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getLong("id")).thenReturn(1L);
        when(resultSet.getString("name")).thenReturn("ADMIN");

        Optional<Role> result = roleDao.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("ADMIN", result.get().getName());
        verify(preparedStatement).setLong(1, 1L);
        verify(preparedStatement).executeQuery();
    }

    @Test
    void testFindByIdReturnsEmpty() throws Exception {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        Optional<Role> result = roleDao.findById(2L);

        assertFalse(result.isPresent());
    }

    @Test
    void testFindByNameReturnsRole() throws Exception {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getLong("id")).thenReturn(1L);
        when(resultSet.getString("name")).thenReturn("ADMIN");

        Optional<Role> result = roleDao.findByName("ADMIN");

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        verify(preparedStatement).setString(1, "ADMIN");
        verify(preparedStatement).executeQuery();
    }

    @Test
    void testFindAllReturnsRoles() throws Exception {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getLong("id")).thenReturn(1L);
        when(resultSet.getString("name")).thenReturn("ADMIN");

        List<Role> roles = roleDao.findAll();

        assertEquals(1, roles.size());
        assertEquals("ADMIN", roles.get(0).getName());
        verify(preparedStatement).executeQuery();
    }

    @Test
    void testUpdateRole() throws Exception {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        assertDoesNotThrow(() -> roleDao.update(sampleRole));
        verify(preparedStatement).setString(1, "ADMIN");
        verify(preparedStatement).setLong(2, 1L);
        verify(preparedStatement).executeUpdate();
    }

    @Test
    void testDeleteRole() throws Exception {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        assertDoesNotThrow(() -> roleDao.delete(1L));
        verify(preparedStatement).setLong(1, 1L);
        verify(preparedStatement).executeUpdate();
    }
}
