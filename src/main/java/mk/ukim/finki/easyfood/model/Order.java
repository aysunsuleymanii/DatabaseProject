package mk.ukim.finki.easyfood.model;


import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address address;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @ManyToOne
    @JoinColumn(name = "deliveryman_id")
    private DeliveryMan deliveryMan;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Customer customer;

    @Column(name = "order_date")
    private LocalDateTime orderDate;

    @Column(name = "comment", columnDefinition = "TEXT")
    private String comment;

    @Column(name = "order_status", length = 50)
    private String orderStatus;

    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    // getters and setters
}

