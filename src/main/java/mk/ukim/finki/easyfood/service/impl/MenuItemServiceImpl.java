package mk.ukim.finki.easyfood.service.impl;

import mk.ukim.finki.easyfood.model.Menu;
import mk.ukim.finki.easyfood.model.MenuItem;
import mk.ukim.finki.easyfood.model.exceptions.MenuNotFoundException;
import mk.ukim.finki.easyfood.repository.MenuItemRepository;
import mk.ukim.finki.easyfood.service.MenuItemService;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
