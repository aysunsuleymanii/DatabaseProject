package mk.ukim.finki.easyfood.service.impl;

import mk.ukim.finki.easyfood.model.*;
import mk.ukim.finki.easyfood.repository.*;
import mk.ukim.finki.easyfood.service.AdminService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    private final AppUserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final MenuRepository menuRepository;
    private final ItemRepository itemRepository;
    private final MenuItemRepository menuItemRepository;
    private final OrderRepository ordersRepository;

    public AdminServiceImpl(AppUserRepository userRepository,
                            RestaurantRepository restaurantRepository,
                            MenuRepository menuRepository,
                            ItemRepository itemRepository,
                            MenuItemRepository menuItemRepository,
                            OrderRepository ordersRepository) {
        this.userRepository = userRepository;
        this.restaurantRepository = restaurantRepository;
        this.menuRepository = menuRepository;
        this.itemRepository = itemRepository;
        this.menuItemRepository = menuItemRepository;
        this.ordersRepository = ordersRepository;
    }

    @Override
    public long getTotalUsersCount() {
        return userRepository.count();
    }

    @Override
    public long getTotalRestaurantsCount() {
        return restaurantRepository.count();
    }

    @Override
    public long getTotalMenusCount() {
        return menuRepository.count();
    }

    @Override
    public long getTotalItemsCount() {
        return itemRepository.count();
    }

    @Override
    public List<AppUser> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public AppUser getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    @Override
    public void updateUser(Long id, String firstName, String lastName, String email, String phone) {
        AppUser user = getUserById(id);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPhone(phone);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }

        ordersRepository.setUserIdToNull(id);

        userRepository.deleteById(id);
    }

    @Override
    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    @Override
    public Restaurant getRestaurantById(Long id) {
        return restaurantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurant not found with id: " + id));
    }

    @Override
    public void updateRestaurant(Long id, String name, String websiteUrl, String phoneNumber) {
        Restaurant restaurant = getRestaurantById(id);
        restaurant.setName(name);
        restaurant.setWebsiteUrl(websiteUrl);
        restaurant.setPhoneNumber(phoneNumber);
        restaurantRepository.save(restaurant);
    }

    @Override
    @Transactional
    public void deleteRestaurant(Long id) {
        if (!restaurantRepository.existsById(id)) {
            throw new RuntimeException("Restaurant not found with id: " + id);
        }
        ordersRepository.setRestaurantIdToNull(id);
        restaurantRepository.deleteById(id);
    }

    @Override
    public List<Menu> getAllMenus() {
        return menuRepository.findAll();
    }

    @Override
    public Menu getMenuById(Long id) {
        return menuRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Menu not found with id: " + id));
    }

    @Override
    public void updateMenu(Long id, String name, Long restaurantId) {
        Menu menu = getMenuById(id);
        Restaurant restaurant = getRestaurantById(restaurantId);

        menu.setName(name);
        menu.setRestaurant(restaurant);
        menu.setUpdatedAt(LocalDateTime.now());
        menuRepository.save(menu);
    }

    @Override
    @Transactional
    public void deleteMenu(Long id) {
        if (!menuRepository.existsById(id)) {
            throw new RuntimeException("Menu not found with id: " + id);
        }
        List<MenuItem> menuItems = menuItemRepository.findByMenuId(id);
        for (MenuItem menuItem : menuItems) {
            deleteItem(menuItem.getMenu().getId());
        }
        menuRepository.deleteById(id);
    }
    @Override
    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    @Override
    public Item getItemById(Long id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found with id: " + id));
    }

    @Override
    public void updateItem(Long id, String name, String description, String price, String imageUrl) {
        Item item = getItemById(id);
        item.setName(name);
        item.setDescription(description);
        item.setPrice(new BigDecimal(price));
        item.setImageUrl(imageUrl);
        itemRepository.save(item);
    }

    @Override
    @Transactional
    public void deleteItem(Long id) {
        if (!itemRepository.existsById(id)) {
            throw new RuntimeException("Item not found with id: " + id);
        }
    }
}