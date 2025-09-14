package mk.ukim.finki.easyfood.service.impl;

import mk.ukim.finki.easyfood.model.Menu;
import mk.ukim.finki.easyfood.model.Restaurant;
import mk.ukim.finki.easyfood.model.exceptions.InvalidArgumentsException;
import mk.ukim.finki.easyfood.model.exceptions.InvalidMenuIdException;
import mk.ukim.finki.easyfood.repository.MenuRepository;
import mk.ukim.finki.easyfood.service.MenuService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository;

    public MenuServiceImpl(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @Override
    public List<Menu> findAll() {
        return menuRepository.findAll();
    }

    @Override
    public Menu findById(Long id) {
        if (id == null) {
            throw new InvalidArgumentsException("Menu ID cannot be null");
        }
        return menuRepository.findById(id).orElse(null);
    }

    @Override
    public List<Menu> findByRestaurant(Restaurant restaurant) {
        if (restaurant == null) {
            throw new InvalidArgumentsException("Restaurant cannot be null");
        }

        // Since each restaurant has only one menu, return a list with single menu
        Menu menu = menuRepository.findByRestaurant(restaurant);
        return menu != null ? List.of(menu) : List.of();
    }

    @Override
    public Menu save(Menu menu) {
        if (menu == null) {
            throw new InvalidArgumentsException("Menu cannot be null");
        }

        // Validate required fields
        validateMenu(menu);

        // Check if restaurant already has a menu (only for new menus)
        if (menu.getId() == null && hasExistingMenu(menu.getRestaurant())) {
            throw new InvalidArgumentsException("Restaurant already has a menu. Each restaurant can have only one menu.");
        }

        return menuRepository.save(menu);
    }

    @Override
    public void delete(Long id) {
        if (id == null) {
            throw new InvalidArgumentsException("Menu ID cannot be null");
        }

        menuRepository.deleteById(id);
    }

    /**
     * Get the single menu for a restaurant by restaurant ID
     */
    public Menu getMenuByRestaurantId(Long restaurantId) {
        if (restaurantId == null) {
            throw new InvalidArgumentsException("Restaurant ID cannot be null");
        }
        return menuRepository.findByRestaurantId(restaurantId);
    }

    /**
     * Get the single menu for a restaurant
     */
    public Menu getMenuByRestaurant(Restaurant restaurant) {
        if (restaurant == null) {
            throw new InvalidArgumentsException("Restaurant cannot be null");
        }
        return menuRepository.findByRestaurant(restaurant);
    }

    /**
     * Create a new menu for a restaurant
     */
    public Menu createMenu(Menu menu) {
        if (menu == null) {
            throw new InvalidArgumentsException("Menu cannot be null");
        }

        // Validate required fields
        validateMenu(menu);

        // Check if restaurant already has a menu
        if (hasExistingMenu(menu.getRestaurant())) {
            throw new InvalidArgumentsException("Restaurant already has a menu. Each restaurant can have only one menu.");
        }

        // Ensure it's a new menu (no ID)
        menu.setId(null);

        return menuRepository.save(menu);
    }

    /**
     * Update an existing menu
     */
    public Menu updateMenu(Menu menu) {
        if (menu == null) {
            throw new InvalidArgumentsException("Menu cannot be null");
        }

        if (menu.getId() == null) {
            throw new InvalidArgumentsException("Menu ID is required for update");
        }

        Optional<Menu> existingMenu = menuRepository.findById(menu.getId());
        validateMenu(menu);

        return menuRepository.save(menu);
    }

    public void deleteMenu(Long id) {
        delete(id);
    }

    public boolean hasExistingMenu(Restaurant restaurant) {
        if (restaurant == null) {
            return false;
        }
        return menuRepository.existsByRestaurant(restaurant);
    }

    public Menu findByRestaurantAndName(Restaurant restaurant, String name) {
        if (restaurant == null || name == null || name.trim().isEmpty()) {
            return null;
        }
        return menuRepository.findByRestaurantAndName(restaurant, name.trim());
    }

    public boolean existsById(Long id) {
        if (id == null) {
            return false;
        }
        return menuRepository.existsById(id);
    }


    public long count() {
        return menuRepository.count();
    }

    /**
     * Find menu by ID with exception if not found
     */
    public Menu findByIdOrThrow(Long id) {
        return menuRepository.findById(id).orElseThrow();
    }

    /**
     * Get all restaurants that have menus
     */
    public List<Restaurant> getRestaurantsWithMenus() {
        return menuRepository.findAllRestaurantsWithMenus();
    }

    /**
     * Count items in a restaurant's menu
     */
    public int countItemsInRestaurantMenu(Restaurant restaurant) {
        if (restaurant == null) {
            return 0;
        }
        Menu menu = getMenuByRestaurant(restaurant);
        return menu != null ? menuRepository.countItemsInMenu(menu.getId()) : 0;
    }

    private void validateMenu(Menu menu) {
        if (menu.getName() == null || menu.getName().trim().isEmpty()) {
            throw new InvalidArgumentsException("Menu name is required");
        }

        if (menu.getRestaurant() == null) {
            throw new InvalidArgumentsException("Restaurant is required for menu");
        }

        if (menu.getName().trim().length() > 100) {
            throw new InvalidArgumentsException("Menu name cannot exceed 100 characters");
        }

    }
}