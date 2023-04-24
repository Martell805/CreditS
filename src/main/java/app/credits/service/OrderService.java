package app.credits.service;

import app.credits.exception.OrderNotFoundException;
import app.credits.entity.Order;
import app.credits.model.OrderCreation;
import app.credits.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;

    public Order getById(Long id) {
        return orderRepository.findById(id).orElseThrow(
                () -> new OrderNotFoundException("ORDER_NOT_FOUND")
        );
    }

    public Order getByOrderId(String orderId) {
        return orderRepository.findByOrderId(orderId).orElseThrow(
                () -> new OrderNotFoundException("ORDER_NOT_FOUND")
        );
    }

    public List<Order> getAll() {
        return orderRepository.findAll();
    }

    public List<Order> getAllByUserId(Long id) {
        return orderRepository.getAllByUserId(id);
    }

    public Order add(Order order) {
        return orderRepository.save(order);
    }

    public Order add(OrderCreation orderCreation) {
        return orderRepository.save(new Order(
                0L,
                UUID.randomUUID().toString(),
                orderCreation.getUserId(),
                orderCreation.getTariffId(),
                (double) (Math.round(Math.random() * 0.8 + 0.1 * 100) / 100),
                "IN_PROGRESS",
                new Date(),
                new Date()
        ));
    }

    public Order edit(Order order) {
        return orderRepository.save(order);
    }

    public Order delete(Order order) {
        orderRepository.delete(order);
    
        return order;
    }
}
