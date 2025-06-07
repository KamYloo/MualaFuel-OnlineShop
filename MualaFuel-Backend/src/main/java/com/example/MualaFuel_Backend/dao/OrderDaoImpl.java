package com.example.MualaFuel_Backend.dao;

import com.example.MualaFuel_Backend.entity.*;
import com.example.MualaFuel_Backend.enums.OrderStatus;
import com.example.MualaFuel_Backend.factory.ConnectionFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OrderDaoImpl implements OrderDao {

    private final OrderItemDaoImpl orderItemRepository;
    private final UserDao userDao;

    @Override
    public Order save(Order order){
        String insert = "INSERT INTO orders (total_amount, status, user_id, order_date, " +
                "shipping_street, shipping_city, shipping_zip_code, shipping_country, " +
                "payment_method, payment_status, payment_transaction_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        String update = "UPDATE orders SET total_amount = ?, status = ?, user_id = ?, order_date = ?, " +
                "shipping_street = ?, shipping_city = ?, shipping_zip_code = ?, shipping_country = ?, " +
                "payment_method = ?, payment_status = ?, payment_transaction_id = ? " +
                "WHERE id = ?";

        try(Connection conn = ConnectionFactory.getConnection()) {
            if(order.getId() == null) {
                try (PreparedStatement ps = conn.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setBigDecimal(1, order.getTotalAmount());
                    ps.setString(2, order.getStatus().name());
                    ps.setLong(3, order.getUser().getId());
                    ps.setDate(4, java.sql.Date.valueOf(order.getOrderDate()));

                    ps.setString(5, order.getAddress().getShipping_street());
                    ps.setString(6, order.getAddress().getShipping_city());
                    ps.setString(7, order.getAddress().getShipping_zipCode());
                    ps.setString(8, order.getAddress().getShipping_country());

                    ps.setString(9, order.getPaymentDetails().getPayment_method());
                    ps.setString(10, order.getPaymentDetails().getPayment_status());
                    ps.setString(11, order.getPaymentDetails().getPayment_transactionId());

                    ps.executeUpdate();

                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (rs.next()) {
                            order.setId(rs.getLong(1));
                        }
                    }
                }
            } else {
                try (PreparedStatement ps = conn.prepareStatement(update)) {
                    ps.setBigDecimal(1, order.getTotalAmount());
                    ps.setString(2, order.getStatus().name());
                    ps.setLong(3, order.getUser().getId());
                    ps.setDate(4, java.sql.Date.valueOf(order.getOrderDate()));

                    ps.setString(5, order.getAddress().getShipping_street());
                    ps.setString(6, order.getAddress().getShipping_city());
                    ps.setString(7, order.getAddress().getShipping_zipCode());
                    ps.setString(8, order.getAddress().getShipping_country());

                    ps.setString(9, order.getPaymentDetails().getPayment_method());
                    ps.setString(10, order.getPaymentDetails().getPayment_status());
                    ps.setString(11, order.getPaymentDetails().getPayment_transactionId());

                    ps.setLong(12, order.getId());

                    ps.executeUpdate();
                }
            }
        }catch (SQLException ex){
            throw new RuntimeException("Error saving product", ex);
        }
        return order;
    }

    @Override
    public Optional<Order> findById(Long id){
        String select = "select * from orders where id = ?";
        try(Connection con = ConnectionFactory.getConnection()){
            try(PreparedStatement ps = con.prepareStatement(select)){
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
    public List<Order> findByUserId(Long id){
        String select = "select * from orders where user_id = ?";
        List<Order> list = new ArrayList<>();
        try(Connection conn = ConnectionFactory.getConnection()){
            try(PreparedStatement ps = conn.prepareStatement(select)){
                ps.setLong(1, id);
                try(ResultSet rs = ps.executeQuery()){
                    while(rs.next()){
                        Order order = map(rs);

                        list.add(order);
                    }
                }
            }
        }catch (SQLException ex){
            throw new RuntimeException("Error saving product", ex);
        }
        return list;
    }

    @Override
    public List<Order> findAll(){
        String select = "select * from orders";
        List<Order> list = new ArrayList<>();
        try(Connection conn = ConnectionFactory.getConnection()){
            try(PreparedStatement ps = conn.prepareStatement(select)){
                try(ResultSet rs = ps.executeQuery()){
                    while(rs.next()){
                        list.add(map(rs));
                    }
                }
            }
        }catch (SQLException ex){
            throw new RuntimeException("Error saving product", ex);
        }
        return list;
    }

    @Override
    public void delete(Long id){
        String sql = "DELETE FROM orders WHERE id = ?";
        try(Connection conn = ConnectionFactory.getConnection()){
            try(PreparedStatement ps = conn.prepareStatement(sql)){
                ps.setLong(1, id);
                ps.executeUpdate();
            }
        }catch (SQLException ex){
            throw new RuntimeException("Error saving product", ex);
        }
    }

    private Order map(ResultSet rs) throws SQLException {
        ShippingDetails shippingDetails = ShippingDetails.builder()
                .shipping_street(rs.getString("shipping_street"))
                .shipping_city(rs.getString("shipping_city"))
                .shipping_zipCode(rs.getString("shipping_zip_code"))
                .shipping_country(rs.getString("shipping_country"))
                .build();

        PaymentDetails paymentDetails = PaymentDetails.builder()
                .payment_method(rs.getString("payment_method"))
                .payment_status(rs.getString("payment_status"))
                .payment_transactionId(rs.getString("payment_transaction_id"))
                .build();

        List<OrderItem> orderItems = orderItemRepository.findByOrderId(rs.getLong("id"));

        User user = userDao.findById(rs.getLong("user_id")).get();

        return Order.builder()
                .id(rs.getLong("id"))
                .orderItems(orderItems)
                .totalAmount(rs.getBigDecimal("total_amount"))
                .status(OrderStatus.valueOf(rs.getString("status")))
                .orderDate(rs.getDate("order_date").toLocalDate())
                .address(shippingDetails)
                .paymentDetails(paymentDetails)
                .user(user)
                .build();
    }

}
