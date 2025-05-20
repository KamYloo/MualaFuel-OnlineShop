package com.example.MualaFuel_Backend.dao;

import com.example.MualaFuel_Backend.entity.Order;
import com.example.MualaFuel_Backend.entity.OrderItem;
import com.example.MualaFuel_Backend.entity.Product;
import com.example.MualaFuel_Backend.factory.ConnectionFactory;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class OrderItemDao {

    public OrderItem save(OrderItem orderItem) throws SQLException {
        String insert = "INSERT INTO order_item (order_id, product_id, quantity, unit_price) " +
                "VALUES (?, ?, ?, ?)";

        String update = "UPDATE order_item SET order_id = ?, product_id = ?, quantity = ?, unit_price = ? " +
                "WHERE id = ?";

        try(Connection conn = ConnectionFactory.getConnection()) {
            if(orderItem.getId() == null) {
                try (PreparedStatement ps = conn.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setLong(1, orderItem.getOrder().getId());
                    ps.setLong(2, orderItem.getProduct().getId());
                    ps.setInt(3, orderItem.getQuantity());
                    ps.setBigDecimal(4, orderItem.getUnitPrice());

                    ps.executeUpdate();

                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (rs.next()) {
                            orderItem.setId(rs.getLong(1));
                        }
                    }
                }
            } else {
                try (PreparedStatement ps = conn.prepareStatement(update)) {
                    ps.setLong(1, orderItem.getOrder().getId());
                    ps.setLong(2, orderItem.getProduct().getId());
                    ps.setInt(3, orderItem.getQuantity());
                    ps.setBigDecimal(4, orderItem.getUnitPrice());
                    ps.setLong(5, orderItem.getId());

                    ps.executeUpdate();
                }
            }
        }
        return orderItem;
    }

    public List<OrderItem> findByOrderId(long orderId) throws SQLException {
        List<OrderItem> list = new ArrayList<>();
        String sql = "SELECT * FROM order_item WHERE order_id = ?";
        try(Connection conn = ConnectionFactory.getConnection()){
            try(PreparedStatement ps = conn.prepareStatement(sql)){
                ps.setLong(1, orderId);
                try(ResultSet rs = ps.executeQuery()){
                    while(rs.next()){
                        list.add(map(rs));
                    }
                }
            }
        }
        return list;
    }

    public Optional<OrderItem> findById(Long id) throws SQLException  {
        String sql = "SELECT * FROM order_item WHERE order_id = ?";
        try(Connection conn = ConnectionFactory.getConnection()){
            try(PreparedStatement ps = conn.prepareStatement(sql)){
                ps.setLong(1, id);
                try(ResultSet rs = ps.executeQuery()){
                    if(rs.next()){
                        return Optional.of(map(rs));
                    }
                }
            }
        }
        return Optional.empty();
    }

    public void deleteById(Long id) throws SQLException {
        String sql = "DELETE FROM order_item WHERE id = ?";
        try(Connection conn = ConnectionFactory.getConnection()){
            try(PreparedStatement stmt = conn.prepareStatement(sql)){
                stmt.setLong(1, id);
                stmt.executeUpdate();
            }
        }
    }

    private OrderItem map(ResultSet resultSet) throws SQLException {
        OrderItem orderItem = new OrderItem();

        orderItem.setId(resultSet.getLong("id"));
        orderItem.setQuantity(resultSet.getInt("quantity"));
        orderItem.setUnitPrice(resultSet.getBigDecimal("unit_price"));

        Order order = new Order();
        order.setId(resultSet.getLong("order_id"));
        orderItem.setOrder(order);

        Product product = new Product();
        product.setId(resultSet.getLong("product_id"));
        orderItem.setProduct(product);

        return orderItem;
    }

}
