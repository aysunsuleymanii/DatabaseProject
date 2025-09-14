package mk.ukim.finki.easyfood.service;

import mk.ukim.finki.easyfood.model.Menu;
import mk.ukim.finki.easyfood.model.Restaurant;

import java.util.List;
import java.util.Optional;

public interface MenuService {

    public Menu getMenuByRestaurantId(Long restaurantId);

    List<Menu> findAll();

    Menu findById(Long id);

    Menu createMenu(Menu menu);

    Menu updateMenu(Menu menu);

    void deleteMenu(Long id);

    List<Menu> findByRestaurant(Restaurant restaurant);

    Menu save(Menu menu);

    void delete(Long id);
}
