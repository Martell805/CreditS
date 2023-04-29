package app.credits.repository;

import app.credits.entity.Order;
import app.credits.exception.OrderNotFoundException;
import app.credits.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OrderRepository {
    private final JdbcTemplate jdbcTemplate;
    private final MessageService messageService;

    public List<Order> findAll() {
        return jdbcTemplate.query("SELECT * FROM orders", BeanPropertyRowMapper.newInstance(Order.class));
    }

    public List<Order> getAllInProgress() {
        return jdbcTemplate.query("SELECT * FROM orders WHERE status='IN_PROGRESS'", BeanPropertyRowMapper.newInstance(Order.class));
    }

    public Optional<Order> findById(Long id) {
        try {
            Order order = jdbcTemplate.queryForObject("SELECT * FROM orders WHERE id=?", BeanPropertyRowMapper.newInstance(Order.class),
                    id
            );
            return Optional.ofNullable(order);

        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<Order> findByOrderId(String orderId) {
        try {
            Order order = jdbcTemplate.queryForObject("SELECT * FROM orders WHERE order_id=?", BeanPropertyRowMapper.newInstance(Order.class),
                    orderId
            );
            return Optional.ofNullable(order);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<Order> findByUserId(Long userId) {
        try {
            Order order = jdbcTemplate.queryForObject("SELECT * FROM orders WHERE user_id=?", BeanPropertyRowMapper.newInstance(Order.class),
                    userId
            );
            return Optional.ofNullable(order);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Order save(Order order) {
        jdbcTemplate.update("INSERT INTO orders(order_id, user_id, tariff_id, credit_rating, status, time_insert, time_update) VALUES(?, ?, ?, ?, ?, ?, ?)",
                order.getOrderId(),
                order.getUserId(),
                order.getTariffId(),
                order.getCreditRating(),
                order.getStatus().name(),
                order.getTimeInsert(),
                order.getTimeUpdate()
        );

        return findByOrderId(order.getOrderId()).orElseThrow(
                () -> new OrderNotFoundException(messageService.getMessage("exceptions.order_not_found_by_order_id", order.getOrderId()))
        );
    }

    public Order update(Order order) {
        jdbcTemplate.update("UPDATE orders SET order_id=?, user_id=?, tariff_id=?, credit_rating=?, status=?, time_insert=?, time_update=? WHERE id=?",
                order.getOrderId(),
                order.getUserId(),
                order.getTariffId(),
                order.getCreditRating(),
                order.getStatus().name(),
                order.getTimeInsert(),
                order.getTimeUpdate(),
                order.getId()
        );

        return findById(order.getId()).orElseThrow(
                () -> new OrderNotFoundException(messageService.getMessage("exceptions.order_not_found_by_id", order.getId()))
        );
    }

    public Order delete(Order order) {
        Order oldOrder = findByOrderId(order.getOrderId()).orElseThrow(
                () -> new OrderNotFoundException(messageService.getMessage("exceptions.order_not_found_by_order_id", order.getOrderId()))
        );

        jdbcTemplate.update("DELETE FROM orders WHERE id=?",
                order.getId()
        );

        return oldOrder;
    }
}
