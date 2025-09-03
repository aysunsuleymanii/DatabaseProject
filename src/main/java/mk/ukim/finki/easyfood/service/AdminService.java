package mk.ukim.finki.easyfood.service;

import mk.ukim.finki.easyfood.model.AppUser;
import mk.ukim.finki.easyfood.model.Restaurant;
import mk.ukim.finki.easyfood.model.Menu;
import mk.ukim.finki.easyfood.model.Item;

import java.util.List;

public interface AdminService {

    long getTotalUsersCount();

    long getTotalRestaurantsCount();

    long getTotalMenusCount();

    long getTotalItemsCount();

    List<AppUser> getAllUsers();

    AppUser getUserById(Long id);

    void updateUser(Long id, String firstName, String lastName, String email, String phone);

    void deleteUser(Long id);

    List<Restaurant> getAllRestaurants();

    Restaurant getRestaurantById(Long id);

    void updateRestaurant(Long id, String name, String address, String phoneNumber);

    void deleteRestaurant(Long id);

    List<Menu> getAllMenus();

    Menu getMenuById(Long id);

    void updateMenu(Long id, String name, Long restaurantId);

    void deleteMenu(Long id);

    List<Item> getAllItems();

    Item getItemById(Long id);

    void updateItem(Long id, String name, String description, String price, String imageUrl);

    void deleteItem(Long id);
}