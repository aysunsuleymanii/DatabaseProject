package mk.ukim.finki.easyfood.service.impl;

import mk.ukim.finki.easyfood.model.Item;
import mk.ukim.finki.easyfood.model.Menu;
import mk.ukim.finki.easyfood.model.MenuItem;
import mk.ukim.finki.easyfood.model.Restaurant;
import mk.ukim.finki.easyfood.model.exceptions.MenuNotFoundException;
import mk.ukim.finki.easyfood.repository.MenuItemRepository;
import mk.ukim.finki.easyfood.service.MenuItemService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class MenuItemServiceImpl implements MenuItemService {

    private final MenuItemRepository menuItemRepository;

    public MenuItemServiceImpl(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }

    @Override
    public List<MenuItem> findAll() {
        return menuItemRepository.findAll();
    }

    @Override
    public List<MenuItem> findByMenu(Menu menu) {
        return menuItemRepository.findByMenu(menu);
    }

    @Override
    public MenuItem createMenuItem(MenuItem menuItem) {
        return menuItemRepository.save(menuItem);
    }

    @Override
    public void deleteMenuItem(MenuItem menuItem) {
        menuItemRepository.delete(menuItem);
    }

    @Override
    public MenuItem findById(Long id) {
        return this.menuItemRepository.findById(id).orElseThrow(() -> new MenuNotFoundException(id));
    }

    @Override
    public Optional<Restaurant> getRestaurantByItem(Item item) {
        Optional<MenuItem> menuItem = menuItemRepository.findFirstByItem(item);
        if (menuItem.isPresent()) {
            return Optional.ofNullable(menuItem.get().getMenu().getRestaurant());
        }
        return Optional.empty();
    }

    @Override
    public MenuItem save(MenuItem menuItem) {
        return menuItemRepository.save(menuItem);
    }

    @Override
    @Transactional
    public void deleteByItem(Item item) {
        menuItemRepository.deleteByItem(item);
    }

    @Override
    @Transactional
    public void deleteByMenu(Menu menu) {
        menuItemRepository.deleteByMenu(menu);
    }


    @Override
    public List<MenuItem> findByItem(Item item) {
        return menuItemRepository.findByItem(item);
    }

    @Override
    public MenuItem findByMenuAndItem(Menu menu, Item item) {
        return menuItemRepository.findByMenuAndItem(menu, item);
    }
}
