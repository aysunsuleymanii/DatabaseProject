package mk.ukim.finki.easyfood.web.controller;

import mk.ukim.finki.easyfood.model.AppUser;
import mk.ukim.finki.easyfood.model.Customer;
import mk.ukim.finki.easyfood.model.CartItems;
import mk.ukim.finki.easyfood.model.Item;
import mk.ukim.finki.easyfood.service.ItemService;
import mk.ukim.finki.easyfood.service.ShoppingCartService;
import mk.ukim.finki.easyfood.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/cart")
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;
    private final UserService userService;
    private final ItemService itemService;

    public ShoppingCartController(ShoppingCartService shoppingCartService,
                                  UserService userService,
                                  ItemService itemService) {
        this.shoppingCartService = shoppingCartService;
        this.userService = userService;
        this.itemService = itemService;
    }

    @GetMapping
    public String showCart(Model model, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        String userEmail = authentication.getName();
        Optional<Customer> customerOptional = userService.findByEmail(userEmail);

        if (customerOptional.isEmpty()) {
            return "redirect:/login";
        }

        Customer customer = customerOptional.get();
        List<CartItems> items = shoppingCartService.getItemsInCartByCustomerId(customer.getId());
        BigDecimal total = shoppingCartService.totalItemsPrice(customer.getId());

        model.addAttribute("cartItems", items);
        model.addAttribute("total", total);

        return "shopping_cart";
    }

    @PostMapping("/add")
    public String addToCart(@RequestParam Long itemId, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        String userEmail = authentication.getName();
        AppUser appUser = userService.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!(appUser instanceof Customer customer)) {
            throw new RuntimeException("Only customers can add items to cart.");
        }

        Item item = itemService.findById(itemId);
        shoppingCartService.addItemToCart(customer, item, 1);

        return "redirect:/cart";
    }

    @PostMapping("/remove")
    public String removeFromCart(@RequestParam Long itemId, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        String userEmail = authentication.getName();
        AppUser appUser = userService.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!(appUser instanceof Customer customer)) {
            throw new RuntimeException("Only customers can remove items from cart.");
        }

        Item item = itemService.findById(itemId);
        shoppingCartService.removeItemFromCart(customer, item);

        return "redirect:/cart";
    }

    @PostMapping("/update")
    public String updateCart(@RequestParam Long itemId, @RequestParam int quantity, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        String userEmail = authentication.getName();
        AppUser appUser = userService.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!(appUser instanceof Customer customer)) {
            throw new RuntimeException("Only customers can update cart items.");
        }

        Item item = itemService.findById(itemId);
        shoppingCartService.updateItemQuantity(customer, item, quantity);

        return "redirect:/cart";
    }
}