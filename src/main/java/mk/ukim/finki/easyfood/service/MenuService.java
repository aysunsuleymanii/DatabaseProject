package mk.ukim.finki.easyfood.service;

import mk.ukim.finki.easyfood.model.Menu;

public interface MenuService {

    public Menu getMenuByRestaurantId(Long restaurantId);
}
