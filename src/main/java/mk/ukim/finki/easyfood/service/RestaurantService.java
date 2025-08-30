package mk.ukim.finki.easyfood.service;

import mk.ukim.finki.easyfood.model.Menu;
import mk.ukim.finki.easyfood.model.Restaurant;

public interface RestaurantService {
    Restaurant findRestaurantById(Long id);
}
