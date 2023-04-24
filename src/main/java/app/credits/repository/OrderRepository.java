package app.credits.repository;

import app.credits.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> getAllByUserId(Long userId);
    Optional<Order> findByOrderId(String orderId);
    @Query("SELECT order FROM Order order WHERE order.status = 'IN_PROGRESS'")
    List<Order> getAllInProgress();
}
