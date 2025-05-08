package com.example.MualaFuel_Backend.factory;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;

@Slf4j
@Component
public class ConnectionFactory {
    private static HikariConfig config;
    private static HikariDataSource ds;

    static {
        config = new HikariConfig("/hikari.properties");
        ds = new HikariDataSource( config );
    }

    public static Connection getConnection() {
        try {
            return ds.getConnection();
        } catch (SQLException ex) {
            log.error(ex.getMessage());
        }
        return null;
    }
}
