package mk.ukim.finki.easyfood.model;

import java.io.Serializable;
import java.util.Objects;

public class RestaurantCategoryId implements Serializable {
    private Long category;
    private Long restaurant;

    public RestaurantCategoryId() {}

    public RestaurantCategoryId(Long category, Long restaurant) {
        this.category = category;
        this.restaurant = restaurant;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RestaurantCategoryId)) return false;
        RestaurantCategoryId that = (RestaurantCategoryId) o;
        return Objects.equals(category, that.category) && Objects.equals(restaurant, that.restaurant);
    }

    @Override
    public int hashCode() {
        return Objects.hash(category, restaurant);
    }
}
