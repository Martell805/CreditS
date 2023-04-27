package app.credits.controller;

import app.credits.entity.Order;
import app.credits.entity.User;
import app.credits.exception.*;
import app.credits.model.OrderCreation;
import app.credits.model.OrderDeletion;
import app.credits.service.OrderService;
import app.credits.service.OrderStatusService;
import lombok.RequiredArgsConstructor;
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

    @GetMapping("/getOrders")
    public ResponseEntity<List<Order>> getAll(){
        return ResponseEntity.ok(orderService.getAll());
    }

    @GetMapping("order/{id}")
    public ResponseEntity<Order> get(@PathVariable Long id){
        return ResponseEntity.ok(orderService.getById(id));
    }

    @GetMapping("order")
    public ResponseEntity<Order> get(@RequestParam String orderId){
        return ResponseEntity.ok(orderService.getByOrderId(orderId));
    }

    @GetMapping("order/me")
    public ResponseEntity<Order> get(Authentication authentication){
        User user = (User) authentication.getPrincipal();

        return ResponseEntity.ok(orderService.getByUserId(user.getId()));
    }

    @GetMapping("/getStatusOrder")
    public ResponseEntity<String> getStatusOrder(@RequestParam String orderId){
        return ResponseEntity.ok(orderStatusService.getStatusOrder(orderId));
    }

    @GetMapping("/getStatusOrder/me")
    public ResponseEntity<String> getStatusOrder(Authentication authentication){
        User user = (User) authentication.getPrincipal();
        Order order = orderService.getByUserId(user.getId());

        return ResponseEntity.ok(orderStatusService.getStatusOrder(order.getOrderId()));
    }

    @PostMapping("order")
    public ResponseEntity<Order> post(@RequestBody OrderCreation orderCreation){
        return ResponseEntity.ok(orderService.addOrderByOrderCreation(orderCreation));
    }

    @PostMapping("order/me")
    public ResponseEntity<Order> post(@RequestParam Long tariffId,
                                      Authentication authentication){
        User user = (User) authentication.getPrincipal();

        return ResponseEntity.ok(orderService.addOrderByOrderCreation(
                new OrderCreation(
                        user.getId(),
                        tariffId
                )
        ));
    }

    @PatchMapping("order")
    public ResponseEntity<Order> patch(@RequestBody Order order){
        return ResponseEntity.ok(orderService.edit(order));
    }

    @DeleteMapping("order")
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
