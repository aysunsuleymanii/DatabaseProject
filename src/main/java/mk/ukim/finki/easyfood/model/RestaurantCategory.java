package mk.ukim.finki.easyfood.model;


import jakarta.persistence.*;

@Entity
@Table(name = "restaurant_category")
@IdClass(RestaurantCategoryId.class)
public class RestaurantCategory {

    @Id
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Id
    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    // getters and setters
}
