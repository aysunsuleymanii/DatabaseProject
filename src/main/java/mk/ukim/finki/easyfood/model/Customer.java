package mk.ukim.finki.easyfood.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import mk.ukim.finki.easyfood.model.enumerations.ROLE;

import java.util.List;


@Entity
@Table(name = "customer")
@PrimaryKeyJoinColumn(name = "user_id")
@Data
@NoArgsConstructor
public class Customer extends AppUser {


    @OneToMany(mappedBy = "customer")
    private List<Order> orders;

    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL)
    private ShoppingCart shoppingCart;


    public Customer(String email, String password, String firstName, String lastName, String phone, ROLE role) {
        super(email, password, firstName, lastName, phone, role);
    }

}

