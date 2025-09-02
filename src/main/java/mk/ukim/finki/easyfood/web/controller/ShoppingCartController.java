package mk.ukim.finki.easyfood.web.controller;

import mk.ukim.finki.easyfood.model.CartItems;
import mk.ukim.finki.easyfood.model.Item;
import mk.ukim.finki.easyfood.service.ShoppingCartService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/shoppingCart")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    public ShoppingCartController(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }

    @GetMapping("/{id}")
    public String shoppingCart(@PathVariable("id") Long id, Model model) {
        List<CartItems> items= shoppingCartService.getItemsInCartByCustomerId(id);
        BigDecimal total = shoppingCartService.totalItemsPrice(id);
        model.addAttribute("items", items);
        model.addAttribute("total", total);
        return "shopping_cart";
    }
}
