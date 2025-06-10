package com.example.MualaFuel_Backend.dao;

import com.example.MualaFuel_Backend.entity.Order;
import com.example.MualaFuel_Backend.entity.OrderItem;
import com.example.MualaFuel_Backend.entity.Product;
import com.example.MualaFuel_Backend.factory.ConnectionFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OrderItemDaoImpl implements OrderItemDao {

    private final ProductDao productDao;

    @Override
    public OrderItem save(OrderItem orderItem) {
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
        }catch (SQLException ex){
            throw new RuntimeException("Error saving product", ex);
        }
        return orderItem;
    }

    @Override
    public List<OrderItem> findByOrderId(long orderId) {
        List<OrderItem> list = new ArrayList<>();
        String sql = "SELECT * FROM order_item WHERE order_id = ?";
        try(Connection conn = ConnectionFactory.getConnection()){
            try(PreparedStatement ps = conn.prepareStatement(sql)){
                ps.setLong(1, orderId);
                try(ResultSet rs = ps.executeQuery()){
                    while(rs.next()){
                        OrderItem item = map(rs);
                        Product product = productDao.findById(item.getProduct().getId()).get();
                        item.setProduct(product);
                        list.add(item);
                    }
                }
            }
        }catch (SQLException ex){
            throw new RuntimeException("Error saving product", ex);
        }
        return list;
    }

    @Override
    public Optional<OrderItem> findById(Long id)  {
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
        }catch (SQLException ex){
            throw new RuntimeException("Error saving product", ex);
        }
        return Optional.empty();
    }

    @Override
    public List<OrderItem> findAll() {
        String sql = "SELECT * FROM order_item";
        List<OrderItem> orderItems = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                OrderItem orderItem = map(rs);
                Optional<Product> product = productDao.findById(orderItem.getProduct().getId());
                product.ifPresent(orderItem::setProduct);
                orderItems.add(orderItem);
            }

        } catch (SQLException ex) {
            throw new RuntimeException("Error fetching all order items", ex);
        }

        return orderItems;
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM order_item WHERE id = ?";
        try(Connection conn = ConnectionFactory.getConnection()){
            try(PreparedStatement stmt = conn.prepareStatement(sql)){
                stmt.setLong(1, id);
                stmt.executeUpdate();
            }
        }catch (SQLException ex){
            throw new RuntimeException("Error saving product", ex);
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
