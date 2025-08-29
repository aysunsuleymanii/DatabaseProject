package mk.ukim.finki.easyfood.model;

import jakarta.persistence.*;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "restaurant")
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "restaurant_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "address_id")
    private Address address;

    @ManyToMany(mappedBy = "restaurants")
    private List<RestaurantOwner> owners;

    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @Column(name = "website_url", length = 255)
    private String websiteUrl;

    @Column(name = "opening_time")
    private LocalTime openingTime;

    @Column(name = "closing_time")
    private LocalTime closingTime;

    @Column(name = "email", length = 150)
    private String email;

    @Column(name = "phone_number", length = 50)
    private String phoneNumber;


    // getters and setters
}
