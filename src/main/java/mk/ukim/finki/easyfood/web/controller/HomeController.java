package mk.ukim.finki.easyfood.web.controller;

import mk.ukim.finki.easyfood.model.Category;
import mk.ukim.finki.easyfood.model.Item;
import mk.ukim.finki.easyfood.service.CategoryService;
import mk.ukim.finki.easyfood.service.ItemService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping(path = {"/", "", "/home"})
public class HomeController {

    private final CategoryService categoryService;
    private final ItemService itemService;

    public HomeController(CategoryService categoryService, ItemService itemService) {
        this.categoryService = categoryService;
        this.itemService = itemService;
    }

//    @GetMapping
//    public String getHomePage(Model model) {
//        model.addAttribute("bodyContent", "home");
//        return "main_pg";
//    }

    @GetMapping("/")
    public String getHomePage(Model model) {
        List<Category> categories = categoryService.listCategories();
        List<Item> items = itemService.findAll();
        List<Item> recommendedItems = itemService.findRecommendedItems();
        model.addAttribute("categories", categories);
        model.addAttribute("items", items);
        model.addAttribute("recommendedItems", recommendedItems);
        return "main_pg";
    }


}

