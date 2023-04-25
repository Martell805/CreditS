package app.credits.controller;

import app.credits.entity.Order;
import app.credits.entity.User;
import app.credits.exception.*;
import app.credits.model.OrderCreation;
import app.credits.model.OrderDeletion;
import app.credits.service.OrderService;
import app.credits.service.OrderStatusService;
import app.credits.service.TariffService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/loan-service")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final OrderStatusService orderStatusService;
    private final TariffService tariffService;

    @GetMapping("/getOrders")
    public ResponseEntity<List<Order>> getAll(){
        return ResponseEntity.ok(orderService.getAll());
    }

    @GetMapping("orders/{id}")
    public ResponseEntity<Order> get(@PathVariable Long id){
        return ResponseEntity.ok(orderService.getById(id));
    }

    @GetMapping("orders/{orderId}")
    public ResponseEntity<Order> get(@PathVariable String orderId){
        return ResponseEntity.ok(orderService.getByOrderId(orderId));
    }

    @GetMapping("/getStatusOrder")
    public ResponseEntity<String> getStatusOrder(@RequestParam String orderId){
        return ResponseEntity.ok(orderStatusService.getStatusOrder(orderId));
    }

    @PostMapping("orders")
    public ResponseEntity<Order> post(@RequestBody OrderCreation orderCreation){
        tariffService.getById(orderCreation.getTariffId());

        Order order;

        try {
            order = orderService.getByUserId(orderCreation.getUserId());
        } catch (OrderNotFoundException e) {
            return ResponseEntity.ok(orderService.add(orderCreation));
        }

        throw switch (order.getStatus()) {
            case "IN_PROGRESS" -> new LoanConservationException("У пользователя с id " + orderCreation.getUserId() + " уже есть завяка на кредит на рассмотрении с id заявки " + order.getOrderId());
            case "APPROVED" -> new LoanAlreadyApprovedException("У пользователя с id " + orderCreation.getUserId() + " уже есть одобренная завяка на кредит с id заявки " + order.getOrderId());
            case "REFUSED" -> new TryLaterException("У пользователя с id " + orderCreation.getUserId() + " уже есть отклонённая завяка на кредит с id заявки " + order.getOrderId());
            default -> new CorruptedOrderException("У пользователя с id " + orderCreation.getUserId() + " уже есть испорченная завяка на кредит с id заявки " + order.getOrderId());
        };
    }

    @PostMapping("orders/me")
    public ResponseEntity<Order> post(@RequestParam Long tariffId,
                                      Authentication authentication){
        User user = (User) authentication.getPrincipal();

        return post(new OrderCreation(
                user.getId(),
                tariffId
        ));
    }

    @PatchMapping("orders")
    public ResponseEntity<Order> patch(@RequestBody Order order){
        return ResponseEntity.ok(orderService.edit(order));
    }

    @DeleteMapping("orders")
    public ResponseEntity<Order> delete(@RequestBody Order order){
        return ResponseEntity.ok(orderService.delete(order));
    }

    @DeleteMapping("/deleteOrder")
    public ResponseEntity<Void> deleteOrder(@RequestBody OrderDeletion orderDeletion){
        Order order;

        order = orderService.getByOrderId(orderDeletion.getOrderId());

        if(!order.getUserId().equals(orderDeletion.getUserId())) {
            throw new OrderNotFoundException("Заявка с id пользователя " + orderDeletion.getUserId() + " не найдена");
        }

        if(order.getStatus().equals("IN_PROGRESS")) {
            throw new OrderImpossibleToDeleteException("Заявка с id заявки " + orderDeletion.getOrderId() + " в процессе рассмотрения");
        }

        orderService.delete(order);

        return ResponseEntity.ok().build();
    }
}
