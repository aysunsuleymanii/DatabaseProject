package mk.ukim.finki.easyfood.web.controller;

import mk.ukim.finki.easyfood.model.AppUser;
import mk.ukim.finki.easyfood.model.Restaurant;
import mk.ukim.finki.easyfood.model.Menu;
import mk.ukim.finki.easyfood.model.Item;
import mk.ukim.finki.easyfood.service.AdminService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminHomeController {

    private final AdminService adminService;

    public AdminHomeController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping
    public String adminHome(Model model) {
        // Get counts for dashboard overview
        model.addAttribute("totalUsers", adminService.getTotalUsersCount());
        model.addAttribute("totalRestaurants", adminService.getTotalRestaurantsCount());
        model.addAttribute("totalMenus", adminService.getTotalMenusCount());
        model.addAttribute("totalItems", adminService.getTotalItemsCount());

        return "admin";
    }

    // Users Management
    @GetMapping("/users")
    public String getAllUsers(Model model) {
        List<AppUser> users = adminService.getAllUsers();
        model.addAttribute("users", users);
        return "admin_users";
    }

    @GetMapping("/users/edit/{id}")
    public String editUser(@PathVariable Long id, Model model) {
        AppUser user = adminService.getUserById(id);
        if (user != null) {
            model.addAttribute("user", user);
            return "admin_edit_user";
        } else {
            return "redirect:/admin/users?error=User not found";
        }
    }

    @PostMapping("/users/update")
    public String updateUser(@RequestParam Long id,
                             @RequestParam String firstName,
                             @RequestParam String lastName,
                             @RequestParam String email,
                             @RequestParam String phone,
                             Model model) {
        try {
            adminService.updateUser(id, firstName, lastName, email, phone);
            return "redirect:/admin/users?success=User updated successfully";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/admin/users?error=" + e.getMessage();
        }
    }

    @PostMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        adminService.deleteUser(id);
        return "redirect:/admin/users?success=User deleted successfully";
    }

    // Restaurants Management
    @GetMapping("/restaurants")
    public String getAllRestaurants(Model model) {
        List<Restaurant> restaurants = adminService.getAllRestaurants();
        model.addAttribute("restaurants", restaurants);
        return "admin_restaurants";
    }

    @GetMapping("/restaurants/edit/{id}")
    public String editRestaurant(@PathVariable Long id, Model model) {
        Restaurant restaurant = adminService.getRestaurantById(id);
        model.addAttribute("restaurant", restaurant);
        return "admin_edit_restaurant";
    }

    @PostMapping("/restaurants/update")
    public String updateRestaurant(@RequestParam Long id,
                                   @RequestParam String name,
                                   @RequestParam String webiste,
                                   @RequestParam String phoneNumber,
                                   Model model) {
        try {
            adminService.updateRestaurant(id, name, webiste, phoneNumber);
            return "redirect:/admin/restaurants?success=Restaurant updated successfully";
        } catch (Exception e) {
            return "redirect:/admin/restaurants?error=" + e.getMessage();
        }
    }

    @PostMapping("/restaurants/delete/{id}")
    public String deleteRestaurant(@PathVariable Long id) {
        adminService.deleteRestaurant(id);
        return "redirect:/admin/restaurants?success=Restaurant deleted successfully";
    }

    // Menus Management
    @GetMapping("/menus")
    public String getAllMenus(Model model) {
        List<Menu> menus = adminService.getAllMenus();
        model.addAttribute("menus", menus);
        return "admin_menus";
    }

    @GetMapping("/menus/edit/{id}")
    public String editMenu(@PathVariable Long id, Model model) {
        Menu menu = adminService.getMenuById(id);
        List<Restaurant> restaurants = adminService.getAllRestaurants();
        model.addAttribute("menu", menu);
        model.addAttribute("restaurants", restaurants);
        return "admin_edit_menu";
    }

    @PostMapping("/menus/update")
    public String updateMenu(@RequestParam Long id,
                             @RequestParam String name,
                             @RequestParam Long restaurantId,
                             Model model) {
        try {
            adminService.updateMenu(id, name, restaurantId);
            return "redirect:/admin/menus?success=Menu updated successfully";
        } catch (Exception e) {
            return "redirect:/admin/menus?error=" + e.getMessage();
        }
    }

    @PostMapping("/menus/delete/{id}")
    public String deleteMenu(@PathVariable Long id) {
        adminService.deleteMenu(id);
        return "redirect:/admin/menus?success=Menu deleted successfully";
    }

    // Items Management
    @GetMapping("/items")
    public String getAllItems(Model model) {
        List<Item> items = adminService.getAllItems();
        model.addAttribute("items", items);
        return "admin_items";
    }

    @GetMapping("/items/edit/{id}")
    public String editItem(@PathVariable Long id, Model model) {
        Item item = adminService.getItemById(id);
        model.addAttribute("item", item);
        return "admin_edit_item";
    }

    @PostMapping("/items/update")
    public String updateItem(@RequestParam Long id,
                             @RequestParam String name,
                             @RequestParam String description,
                             @RequestParam String price,
                             @RequestParam String imageUrl,
                             Model model) {
        try {
            adminService.updateItem(id, name, description, price, imageUrl);
            return "redirect:/admin/items?success=Item updated successfully";
        } catch (Exception e) {
            return "redirect:/admin/items?error=" + e.getMessage();
        }
    }

    @PostMapping("/items/delete/{id}")
    public String deleteItem(@PathVariable Long id) {
        adminService.deleteItem(id);
        return "redirect:/admin/items?success=Item deleted successfully";
    }
}