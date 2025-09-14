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

    public HomeController(CategoryService categoryService,
                          ItemService itemService,
                          ShoppingCartService shoppingCartService) {
        this.categoryService = categoryService;
        this.itemService = itemService;
        this.shoppingCartService = shoppingCartService;
    }

    @GetMapping
    public String getHomePage(Model model,
                              Authentication authentication,
                              @RequestParam(required = false) String searchTerm,
                              @RequestParam(required = false) Long categoryId) {

        // Categories and items
        List<Category> categories = categoryService.listCategories();
        List<Item> items = getItemsBasedOnSearchAndCategory(searchTerm, categoryId, model);

        handleShoppingCartForCustomer(authentication, model);

        Long userId = getUserIdIfCustomer(authentication);
        List<Item> recommendedItems = itemService.findRecommendedItems(userId);

        model.addAttribute("categories", categories);
        model.addAttribute("items", items);
        model.addAttribute("recommendedItems", recommendedItems);

        return "main_pg";
    }

    private List<Item> getItemsBasedOnSearchAndCategory(String searchTerm, Long categoryId, Model model) {
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            List<Item> items = itemService.searchItems(searchTerm);
            model.addAttribute("searchTerm", searchTerm);
            return items;
        } else if (categoryId != null) {
            return itemService.findItemsByCategoryId(categoryId);
        }
        return itemService.findAll();
    }

    private void handleShoppingCartForCustomer(Authentication authentication, Model model) {
        Long userId = getUserIdIfCustomer(authentication);
        int numberOfItems = 0;
        if (userId != null) {
            numberOfItems = shoppingCartService.getNumberOfItemsInCart(userId);
        }
        model.addAttribute("numberOfItems", numberOfItems);
    }

    private Long getUserIdIfCustomer(Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof Customer customer) {
            return customer.getId();
        }
        return null;
    }
}
