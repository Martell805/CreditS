package app.credits.controller;

import app.credits.entity.Order;
import app.credits.entity.Tariff;
import app.credits.entity.User;
import app.credits.exception.OrderNotFoundException;
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

    @GetMapping("orders/me")
    public ResponseEntity<List<Order>> getAllMine(Authentication authentication){
        User user = (User) authentication.getPrincipal();

        return ResponseEntity.ok(orderService.getAllByUserId(user.getId()));
    }

    @GetMapping("/getStatusOrder")
    public ResponseEntity<String> getStatusOrder(@RequestParam String orderId){
        return ResponseEntity.ok(orderStatusService.getStatusOrder(orderId));
    }

    @PostMapping("orders")
    public ResponseEntity<Order> post(@RequestBody OrderCreation orderCreation){
        Tariff tariff = tariffService.getById(orderCreation.getTariffId());

        List<Order> orders = orderService.getAllByUserId(orderCreation.getUserId());

        if (!orders.isEmpty()) {
            return switch (orders.get(0).getStatus()) {
                case "IN_PROGRESS" -> new ResponseEntity("LOAN_CONSIDERATION", HttpStatus.BAD_REQUEST);
                case "APPROVED" -> new ResponseEntity("LOAN_ALREADY_APPROVED", HttpStatus.BAD_REQUEST);
                case "REFUSED" -> new ResponseEntity("TRY_LATER", HttpStatus.BAD_REQUEST);
                default -> new ResponseEntity("CORRUPTED_ORDER", HttpStatus.BAD_REQUEST);
            };
        }

        return ResponseEntity.ok(orderService.add(orderCreation));
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

        try {
            order = orderService.getByOrderId(orderDeletion.getOrderId());
        } catch (OrderNotFoundException orderNotFoundException) {
            return new ResponseEntity("ORDER_NOT_FOUND", HttpStatus.BAD_REQUEST);
        }

        if(!order.getUserId().equals(orderDeletion.getUserId())) {
            return new ResponseEntity("ORDER_NOT_FOUND", HttpStatus.BAD_REQUEST);
        }

        if(order.getStatus().equals("IN_PROGRESS")) {
            return new ResponseEntity("ORDER_IMPOSSIBLE_TO_DELETE", HttpStatus.BAD_REQUEST);
        }

        orderService.delete(order);

        return ResponseEntity.ok().build();
    }
}
