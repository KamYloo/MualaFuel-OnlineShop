package com.example.MualaFuel_Backend.service.impl;

import com.example.MualaFuel_Backend.dao.OrderDao;
import com.example.MualaFuel_Backend.dao.OrderItemDao;
import com.example.MualaFuel_Backend.dao.ProductDao;
import com.example.MualaFuel_Backend.dao.UserDao;
import com.example.MualaFuel_Backend.dto.OrderDto;
import com.example.MualaFuel_Backend.entity.*;
import com.example.MualaFuel_Backend.enums.OrderStatus;
import com.example.MualaFuel_Backend.handler.BusinessErrorCodes;
import com.example.MualaFuel_Backend.handler.CustomException;
import com.example.MualaFuel_Backend.mapper.Mapper;
import com.example.MualaFuel_Backend.service.OrderService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.Principal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final Cart cart;
    private final OrderDao orderRepository;
    private final ProductDao productRepository;
    private final OrderItemDao orderItemRepository;
    private final UserDao userRepository;
    private final Mapper<Order, OrderDto> mapper;

    @Override
    public OrderDto placeOrder(ShippingDetails shippingDetails,
                               PaymentDetails paymentDetails,
                               Principal principal) throws SQLException {

        List<CartItem> cartItems = cart.getItems();
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cannot place empty order");
        }

        User user = userRepository.findByEmail(principal.getName()).orElseThrow(
                () -> new CustomException(BusinessErrorCodes.NOT_FOUND)
        );

        Order order = Order.builder()
                .user(user)
                .status(OrderStatus.NEW)
                .address(shippingDetails)
                .paymentDetails(paymentDetails)
                .build();

        Order savedOrder = orderRepository.save(order);

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (CartItem cartItem : cartItems) {
            Product product = productRepository.findById(cartItem.getProductId())
                    .orElseThrow(() -> new CustomException(BusinessErrorCodes.NOT_FOUND));

            if (product.getQuantity() < cartItem.getQuantity()) {
                throw new RuntimeException("Insufficient stock for product: " + product.getName());
            }

            OrderItem orderItem = OrderItem.builder()
                    .product(product)
                    .quantity(cartItem.getQuantity())
                    .order(savedOrder)
                    .unitPrice(product.getPrice())
                    .build();

            product.setQuantity(product.getQuantity() - cartItem.getQuantity());
            productRepository.update(product);

            orderItems.add(orderItem);
            orderItemRepository.save(orderItem);

            total = total.add(product.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
        }

        savedOrder.setTotalAmount(total);
        savedOrder.setOrderItems(orderItems);
        orderRepository.save(savedOrder);

        cart.clear();

        return mapper.mapTo(savedOrder);
    }

    @Override
    public List<OrderDto> getAllOrdersOfUser(Principal principal) throws SQLException {
        User user = userRepository.findByEmail(principal.getName()).orElseThrow(
                () -> new CustomException(BusinessErrorCodes.NOT_FOUND)
        );
        List<Order> list = orderRepository.findByUserId(user.getId());
        return list.stream().map(mapper::mapTo).collect(Collectors.toList());
    }
}
