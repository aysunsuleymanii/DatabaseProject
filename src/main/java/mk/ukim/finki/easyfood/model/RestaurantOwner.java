package mk.ukim.finki.easyfood.model;


import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "restaurant_owner")
@PrimaryKeyJoinColumn(name = "user_id")
public class RestaurantOwner extends AppUser {


    @ManyToMany
    @JoinTable(
            name = "restaurant_owners",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "restaurant_id")
    )
    private List<Restaurant> restaurants;
    // getters and setters
}

