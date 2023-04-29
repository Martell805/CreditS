package app.credits.entity;

import app.credits.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

@Entity
@Table(name = "orders")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String orderId;
    private Long userId;
    private Long tariffId;
    private Double creditRating;
    private OrderStatus status;
    private Date timeInsert;
    private Date timeUpdate;
}
