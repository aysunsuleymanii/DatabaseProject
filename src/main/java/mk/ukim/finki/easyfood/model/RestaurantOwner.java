package mk.ukim.finki.easyfood.model;


import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import mk.ukim.finki.easyfood.model.enumerations.ROLE;

import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "restaurant_owner")
@PrimaryKeyJoinColumn(name = "user_id")
@NoArgsConstructor
public class RestaurantOwner extends AppUser {


    @ManyToMany
    @JoinTable(
            name = "restaurant_owners",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "restaurant_id")
    )
    private List<Restaurant> restaurants;
    // getters and setters

    public RestaurantOwner(String email, String password, String firstName, String lastName, String phone, ROLE role) {
        super(email, password, firstName, lastName, phone, role);
    }


    public List<Restaurant> getRestaurants() {
        return restaurants;
    }

    public void setRestaurants(List<Restaurant> restaurants) {
        this.restaurants = restaurants;
    }
}

