package mk.ukim.finki.easyfood.service;

import mk.ukim.finki.easyfood.model.Item;
import mk.ukim.finki.easyfood.model.Menu;
import mk.ukim.finki.easyfood.model.MenuItem;
import mk.ukim.finki.easyfood.model.Restaurant;

import java.util.List;
import java.util.Optional;

public interface MenuItemService {
    List<MenuItem> findAll();

    List<MenuItem> findByMenu(Menu menu);

    MenuItem createMenuItem(MenuItem menuItem);

    void deleteMenuItem(MenuItem menuItem);

    MenuItem findById(Long id);

    public Optional<Restaurant> getRestaurantByItem(Item item);
}

