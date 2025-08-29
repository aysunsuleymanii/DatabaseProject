package mk.ukim.finki.easyfood.model;

import jakarta.persistence.*;

import java.util.List;


@Entity
@Table(name = "customer")
@PrimaryKeyJoinColumn(name = "user_id")  // links PK to AppUser
public class Customer extends AppUser{


    @OneToMany(mappedBy = "customer")
    private List<Order> orders;

    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL)
    private ShoppingCart shoppingCart;

    // getters and setters
}

