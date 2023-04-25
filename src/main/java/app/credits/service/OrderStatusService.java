package app.credits.service;

import app.credits.entity.User;
import app.credits.exception.OrderNotFoundException;
import app.credits.entity.Order;
import app.credits.model.Email;
import app.credits.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class OrderStatusService {
    private final OrderRepository orderRepository;
    private final UserService userService;
    private final EmailService emailService;

    public String getStatusOrder(String orderId) {
        Order order = orderRepository.findByOrderId(orderId).orElseThrow(
                () -> new OrderNotFoundException("Заказ с id " + orderId + "не найден")
        );
        return order.getStatus();
    }

    private void reviewOrder(Order order) {
        if (!order.getStatus().equals("IN_PROGRESS")){
            return;
        }

        if (Math.random() >= 0.5) {
            order.setStatus("APPROVED");
        } else {
            order.setStatus("REFUSED");
        }

        order.setTimeUpdate(new Date());
        orderRepository.update(order);

        User user = userService.getById(order.getUserId());
        emailService.sendIfSubscribed(user, new Email(
                "Статус заявки",
                user.getName() + ", статус вашей заявки " + order.getOrderId() + " изменён на " + order.getStatus()
        ));
    }

    @Scheduled(fixedRate  = 120_000)
    public void reviewOrders() {
        orderRepository.getAllInProgress().forEach(this::reviewOrder);
    }
}
