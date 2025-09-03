package mk.ukim.finki.easyfood.service;

import mk.ukim.finki.easyfood.model.Menu;
import mk.ukim.finki.easyfood.model.MenuItem;

import java.util.List;

public interface MenuItemService {
    List<MenuItem> findAll();

    List<MenuItem> findByMenu(Menu menu);

    MenuItem createMenuItem(MenuItem menuItem);

    void deleteMenuItem(MenuItem menuItem);

    MenuItem findById(Long id);
}

