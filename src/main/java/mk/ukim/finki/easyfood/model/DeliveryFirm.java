package mk.ukim.finki.easyfood.model;


import jakarta.persistence.*;

@Entity
@Table(name = "delivery_firm")
public class DeliveryFirm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "deliveryfirm_id")
    private Long id;

    @Column(name = "name", nullable = false, length = 150)
    private String name;

    // getters and setters
}

