package mk.ukim.finki.easyfood.model;

import jakarta.persistence.*;
import lombok.*;
import mk.ukim.finki.easyfood.model.enumerations.ROLE;
import mk.ukim.finki.easyfood.model.Order;

import java.util.List;


@Entity
@Table(name = "customer")
@PrimaryKeyJoinColumn(name = "user_id")
@Data
public class Customer extends AppUser {
    protected Customer() {
    }

    @OneToMany(mappedBy = "customer")
    private List<Order> orders;

    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL)
    private ShoppingCart shoppingCart;


    public Customer(String email, String password, String firstName, String lastName, String phone, ROLE role) {
        super(email, password, firstName, lastName, phone, role);
    }

    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }

    public List<Order> getOrders() {
        return orders;
    }
}

