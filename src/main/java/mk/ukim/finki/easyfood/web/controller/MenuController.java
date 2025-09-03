package mk.ukim.finki.easyfood.web.controller;

import mk.ukim.finki.easyfood.model.Item;
import mk.ukim.finki.easyfood.model.Menu;
import mk.ukim.finki.easyfood.model.Restaurant;
import mk.ukim.finki.easyfood.service.ItemService;
import mk.ukim.finki.easyfood.service.MenuService;
import mk.ukim.finki.easyfood.service.RestaurantService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/menu")
public class MenuController {
    private final ItemService itemService;
    private final RestaurantService restaurantService;
    private final MenuService menuService;

    public MenuController(ItemService itemService, RestaurantService restaurantService, MenuService menuService) {
        this.itemService = itemService;
        this.restaurantService = restaurantService;
        this.menuService = menuService;

    }

    @GetMapping("/{id}")
    public String showMenu(@PathVariable Long id, Model model) {
        Restaurant restaurant= restaurantService.findRestaurantById(id);
        Menu menu=menuService.getMenuByRestaurantId(id);
        List<Item> items = itemService.getItemsByMenuId(menu.getId());

        model.addAttribute("restaurant", restaurant);
        model.addAttribute("items", items);
        return "restaurant_menu";
    }
}