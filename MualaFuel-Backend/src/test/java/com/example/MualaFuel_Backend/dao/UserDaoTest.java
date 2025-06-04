package com.example.MualaFuel_Backend.dao;

import com.example.MualaFuel_Backend.entity.Role;
import com.example.MualaFuel_Backend.entity.User;
import com.example.MualaFuel_Backend.factory.ConnectionFactory;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserDaoTest {

    @Mock Connection connection;
    @Mock PreparedStatement userStmt;
    @Mock PreparedStatement roleStmt;
    @Mock ResultSet userRs;
    @Mock ResultSet roleRs;

    @InjectMocks UserDao userDao;

    private MockedStatic<ConnectionFactory> connectionFactoryMock;

    User sampleUser;
    Role sampleRole;

    @BeforeEach
    void setUp() throws Exception {
        sampleRole = Role.builder().id(2L).name("USER").build();
        sampleUser = User.builder()
                .id(1L)
                .firstName("Jan")
                .lastName("Kowalski")
                .email("jan@kowalski.pl")
                .password("pass")
                .roles(new HashSet<>(Collections.singletonList(sampleRole)))
                .build();

        connectionFactoryMock = Mockito.mockStatic(ConnectionFactory.class);
        connectionFactoryMock.when(ConnectionFactory::getConnection).thenReturn(connection);
    }

    @AfterEach
    void tearDown() {
        connectionFactoryMock.close();
    }

    @Test
    void testSaveUserWithRoles() throws Exception {
        when(connection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(userStmt);
        when(userStmt.executeUpdate()).thenReturn(1);
        ResultSet generatedKeys = mock(ResultSet.class);
        when(userStmt.getGeneratedKeys()).thenReturn(generatedKeys);
        when(generatedKeys.next()).thenReturn(true);
        when(generatedKeys.getLong(1)).thenReturn(1L);

        when(connection.prepareStatement(startsWith("INSERT INTO user_role"))).thenReturn(roleStmt);
        when(roleStmt.executeBatch()).thenReturn(new int[]{1});

        doNothing().when(connection).commit();

        User result = userDao.save(sampleUser);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(userStmt).setString(1, "Jan");
        verify(userStmt).setString(2, "Kowalski");
        verify(userStmt).setString(3, "jan@kowalski.pl");
        verify(userStmt).setString(4, "pass");
        verify(roleStmt).setLong(1, 1L);
        verify(roleStmt).setLong(2, 2L);
        verify(roleStmt).addBatch();
        verify(roleStmt).executeBatch();
        verify(connection).commit();
    }

    @Test
    void testFindByIdReturnsUserWithRoles() throws Exception {
        when(connection.prepareStatement(contains("FROM \"user\""))).thenReturn(userStmt);
        when(userStmt.executeQuery()).thenReturn(userRs);
        when(userRs.next()).thenReturn(true);
        when(userRs.getLong("id")).thenReturn(1L);
        when(userRs.getString("first_name")).thenReturn("Jan");
        when(userRs.getString("last_name")).thenReturn("Kowalski");
        when(userRs.getString("email")).thenReturn("jan@kowalski.pl");
        when(userRs.getString("password")).thenReturn("pass");

        when(connection.prepareStatement(startsWith("SELECT r.id"))).thenReturn(roleStmt);
        when(roleStmt.executeQuery()).thenReturn(roleRs);
        when(roleRs.next()).thenReturn(true, false);
        when(roleRs.getLong("id")).thenReturn(2L);
        when(roleRs.getString("name")).thenReturn("USER");

        Optional<User> result = userDao.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("jan@kowalski.pl", result.get().getEmail());
        assertEquals(1, result.get().getRoles().size());
        assertEquals("USER", result.get().getRoles().iterator().next().getName());
        verify(userStmt).setLong(1, 1L);
        verify(roleStmt).setLong(1, 1L);
    }

    @Test
    void testFindByIdReturnsEmpty() throws Exception {
        when(connection.prepareStatement(contains("FROM \"user\""))).thenReturn(userStmt);
        when(userStmt.executeQuery()).thenReturn(userRs);
        when(userRs.next()).thenReturn(false);

        Optional<User> result = userDao.findById(123L);

        assertFalse(result.isPresent());
    }

    @Test
    void testFindByEmailReturnsUserWithRoles() throws Exception {
        when(connection.prepareStatement(contains("FROM \"user\""))).thenReturn(userStmt);
        when(userStmt.executeQuery()).thenReturn(userRs);
        when(userRs.next()).thenReturn(true);
        when(userRs.getLong("id")).thenReturn(1L);
        when(userRs.getString("first_name")).thenReturn("Jan");
        when(userRs.getString("last_name")).thenReturn("Kowalski");
        when(userRs.getString("email")).thenReturn("jan@kowalski.pl");
        when(userRs.getString("password")).thenReturn("pass");

        when(connection.prepareStatement(startsWith("SELECT r.id"))).thenReturn(roleStmt);
        when(roleStmt.executeQuery()).thenReturn(roleRs);
        when(roleRs.next()).thenReturn(true, false);
        when(roleRs.getLong("id")).thenReturn(2L);
        when(roleRs.getString("name")).thenReturn("USER");

        Optional<User> result = userDao.findByEmail("jan@kowalski.pl");

        assertTrue(result.isPresent());
        assertEquals("jan@kowalski.pl", result.get().getEmail());
        assertEquals(1, result.get().getRoles().size());
        assertEquals("USER", result.get().getRoles().iterator().next().getName());
        verify(userStmt).setString(1, "jan@kowalski.pl");
        verify(roleStmt).setLong(1, 1L);
    }

    @Test
    void testFindByEmailReturnsEmpty() throws Exception {
        when(connection.prepareStatement(contains("FROM \"user\""))).thenReturn(userStmt);
        when(userStmt.executeQuery()).thenReturn(userRs);
        when(userRs.next()).thenReturn(false);

        Optional<User> result = userDao.findByEmail("notfound@domain.com");

        assertFalse(result.isPresent());
    }
}
