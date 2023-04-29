package app.credits.service;

import app.credits.exception.*;
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

    private final TariffService tariffService;
    private final MessageService messageService;

    public Order getById(Long id) {
        return orderRepository.findById(id).orElseThrow(
                () -> new OrderNotFoundException(messageService.getMessage("exceptions.order_not_found_by_id", id))
        );
    }

    public Order getByOrderId(String orderId) {
        return orderRepository.findByOrderId(orderId).orElseThrow(
                () -> new OrderNotFoundException(messageService.getMessage("exceptions.order_not_found_by_order_id", orderId))
        );
    }

    public Order getByUserId(Long userId) {
        return orderRepository.findByUserId(userId).orElseThrow(
                () -> new OrderNotFoundException(messageService.getMessage("exceptions.order_not_found_by_user_id", userId))
        );
    }

    public List<Order> getAll() {
        return orderRepository.findAll();
    }

    public Order addOrderByOrderCreation(OrderCreation orderCreation) {
        tariffService.getById(orderCreation.getTariffId());

        Order order;

        try {
            order = getByUserId(orderCreation.getUserId());
        } catch (OrderNotFoundException e) {
            return add(orderCreation);
        }

        throw switch (order.getStatus()) {
            case "IN_PROGRESS" -> new LoanConservationException(messageService.getMessage("exceptions.loan_conservation_exception", orderCreation.getUserId(), order.getOrderId()));
            case "APPROVED" -> new LoanAlreadyApprovedException(messageService.getMessage("exceptions.loan_already_approved_exception", orderCreation.getUserId(), order.getOrderId()));
            case "REFUSED" -> new TryLaterException(messageService.getMessage("exceptions.try_later_exception", orderCreation.getUserId(), order.getOrderId()));
            default -> new CorruptedOrderException(messageService.getMessage("exceptions.corrupted_order_exception", orderCreation.getUserId(), order.getOrderId()));
        };
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
                (double) Math.round(Math.random() * 10) / 10 * 0.8 + 0.1,
                "IN_PROGRESS",
                new Date(),
                new Date()
        ));
    }

    public Order edit(Order order) {
        return orderRepository.update(order);
    }

    public Order delete(Order order) {
        orderRepository.delete(order);
    
        return order;
    }
}
