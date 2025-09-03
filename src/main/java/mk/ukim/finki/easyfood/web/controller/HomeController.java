package mk.ukim.finki.easyfood.web.controller;

import mk.ukim.finki.easyfood.model.Category;
import mk.ukim.finki.easyfood.model.Customer;
import mk.ukim.finki.easyfood.model.Item;
import mk.ukim.finki.easyfood.service.CategoryService;
import mk.ukim.finki.easyfood.service.ItemService;
import mk.ukim.finki.easyfood.service.ShoppingCartService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping(path = {"/", "/home"})
public class HomeController {

    private final CategoryService categoryService;
    private final ItemService itemService;
    private final ShoppingCartService shoppingCartService;

    public HomeController(CategoryService categoryService, ItemService itemService, ShoppingCartService shoppingCartService) {
        this.categoryService = categoryService;
        this.itemService = itemService;
        this.shoppingCartService = shoppingCartService;
    }


    @GetMapping
    public String getHomePage(Model model,
                              Authentication authentication,
                              @RequestParam(required = false) String searchTerm,
                              @RequestParam(required = false) Long categoryId) {
        List<Category> categories = categoryService.listCategories();
        List<Item> items;

        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            if (username.equals("admin@easyfood.com") || username.contains("admin")) {
                return "redirect:/admin";
            }
        }

        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            items = itemService.searchItems(searchTerm);
            model.addAttribute("searchTerm", searchTerm);
        } else if (categoryId != null) {
            items = itemService.findItemsByCategoryId(categoryId);
        } else {
            items = itemService.findAll();
        }


        Long userId = null;
        if (authentication != null && authentication.getPrincipal() instanceof Customer) {
            Customer customer = (Customer) authentication.getPrincipal();
            userId = customer.getId();
        }

        int numberOfItems = 0;
        if (userId != null) {
            numberOfItems = shoppingCartService.getNumberOfItemsInCart(userId);
        }

        model.addAttribute("numberOfItems", numberOfItems);
        List<Item> recommendedItems = itemService.findRecommendedItems(userId);

        model.addAttribute("categories", categories);
        model.addAttribute("items", items);
        model.addAttribute("recommendedItems", recommendedItems);

        return "main_pg";
    }


}

