package mk.ukim.finki.easyfood.web.controller;

import mk.ukim.finki.easyfood.model.Item;
import mk.ukim.finki.easyfood.model.Menu;
import mk.ukim.finki.easyfood.model.MenuItem;
import mk.ukim.finki.easyfood.model.Restaurant;
import mk.ukim.finki.easyfood.model.RestaurantOwner;
import mk.ukim.finki.easyfood.service.ItemService;
import mk.ukim.finki.easyfood.service.MenuItemService;
import mk.ukim.finki.easyfood.service.MenuService;
import mk.ukim.finki.easyfood.service.RestaurantOwnerService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/restaurantOwner")
public class RestaurantOwnerController {

    private final RestaurantOwnerService restaurantOwnerService;
    private final ItemService itemService;
    private final MenuService menuService;
    private final MenuItemService menuItemService;

    public RestaurantOwnerController(RestaurantOwnerService restaurantOwnerService,
                                     ItemService itemService,
                                     MenuService menuService,
                                     MenuItemService menuItemService) {
        this.restaurantOwnerService = restaurantOwnerService;
        this.itemService = itemService;
        this.menuService = menuService;
        this.menuItemService = menuItemService;
    }

    @GetMapping
    public String getDashboard(Model model, Authentication authentication) {
        RestaurantOwner owner = getRestaurantOwnerIfValid(authentication);
        if (owner == null) {
            return "redirect:/access-denied";
        }

        Restaurant restaurant = owner.getRestaurants().get(0);

        // Get the single menu for this restaurant (one-to-one relationship)
        Menu menu = restaurant.getMenu();
        List<Menu> menus = menu != null ? List.of(menu) : List.of();

        List<Item> allItems = itemService.findByRestaurantMenus(restaurant);

        model.addAttribute("restaurant", restaurant);
        model.addAttribute("menu", menu);
        model.addAttribute("menus", menus);
        model.addAttribute("totalItems", allItems.size());
        model.addAttribute("items", allItems);
        model.addAttribute("owner", owner);

        return "restaurant_owner";
    }

    @GetMapping("/items")
    public String manageItems(Authentication authentication, Model model) {
        RestaurantOwner owner = getRestaurantOwnerIfValid(authentication);
        if (owner == null) {
            return "redirect:/access-denied";
        }

        Restaurant restaurant = owner.getRestaurants().get(0);

        // Get the single menu for this restaurant
        Menu menu = restaurant.getMenu();
        List<Menu> menus = menu != null ? List.of(menu) : List.of();

        List<Item> items = itemService.findByRestaurantMenus(restaurant);

        model.addAttribute("restaurant", restaurant);
        model.addAttribute("menus", menus);
        model.addAttribute("items", items);
        model.addAttribute("newItem", new Item());

        return "restaurant_owner";
    }

    @PostMapping("/items/add")
    public String addItem(@ModelAttribute Item item,
                          @RequestParam("menuId") Long menuId,
                          @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                          Authentication authentication,
                          RedirectAttributes redirectAttributes) {

        RestaurantOwner owner = getRestaurantOwnerIfValid(authentication);
        if (owner == null) {
            return "redirect:/access-denied";
        }

        try {
            Restaurant restaurant = owner.getRestaurants().get(0);

            if (!validateMenuOwnership(menuId, restaurant, redirectAttributes)) {
                return "redirect:/restaurantOwner";
            }

            Menu menu = menuService.findById(menuId);
            handleImageUpload(item, imageFile);

            Item savedItem = itemService.save(item);
            createMenuItemRelationship(menu, savedItem);

            redirectAttributes.addFlashAttribute("success", "Item added successfully!");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error adding item: " + e.getMessage());
        }

        return "redirect:/restaurantOwner";
    }

    @PostMapping("/items/update/{id}")
    public String updateItem(@PathVariable Long id,
                             @ModelAttribute Item item,
                             @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                             Authentication authentication,
                             RedirectAttributes redirectAttributes) {

        RestaurantOwner owner = getRestaurantOwnerIfValid(authentication);
        if (owner == null) {
            return "redirect:/access-denied";
        }

        try {
            Restaurant restaurant = owner.getRestaurants().get(0);
            Item existingItem = itemService.findById(id);

            if (!validateItemOwnership(existingItem, restaurant, redirectAttributes)) {
                return "redirect:/restaurantOwner";
            }

            updateItemDetails(existingItem, item, imageFile);
            itemService.save(existingItem);

            redirectAttributes.addFlashAttribute("success", "Item updated successfully!");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error updating item: " + e.getMessage());
        }

        return "redirect:/restaurantOwner";
    }

    @PostMapping("/items/delete/{id}")
    public String deleteItem(@PathVariable Long id,
                             Authentication authentication,
                             RedirectAttributes redirectAttributes) {

        RestaurantOwner owner = getRestaurantOwnerIfValid(authentication);
        if (owner == null) {
            return "redirect:/access-denied";
        }

        try {
            Restaurant restaurant = owner.getRestaurants().get(0);
            Item item = itemService.findById(id);

            if (!validateItemOwnership(item, restaurant, redirectAttributes)) {
                return "redirect:/restaurantOwner";
            }

            menuItemService.deleteByItem(item);
            itemService.delete(id);

            redirectAttributes.addFlashAttribute("success", "Item deleted successfully!");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting item: " + e.getMessage());
        }

        return "redirect:/restaurantOwner";
    }

    @GetMapping("/items/{id}")
    @ResponseBody
    public Item getItem(@PathVariable Long id, Authentication authentication) {
        RestaurantOwner owner = getRestaurantOwnerIfValid(authentication);
        if (owner == null) {
            return null;
        }

        Restaurant restaurant = owner.getRestaurants().get(0);
        Item item = itemService.findById(id);

        return (item != null && itemService.belongsToRestaurant(item, restaurant)) ? item : null;
    }

    private RestaurantOwner getRestaurantOwnerIfValid(Authentication authentication) {
        if (authentication == null) {
            return null;
        }

        String email = authentication.getName();
        RestaurantOwner owner = restaurantOwnerService.findByEmail(email);

        if (owner == null) {
            return null;
        }

        if (owner.getRestaurants() == null || owner.getRestaurants().isEmpty()) {
            return null;
        }

        return owner;
    }

    private boolean validateMenuOwnership(Long menuId, Restaurant restaurant, RedirectAttributes redirectAttributes) {
        Menu menu = menuService.findById(menuId);
        if (menu == null || !menu.getRestaurant().getId().equals(restaurant.getId())) {
            redirectAttributes.addFlashAttribute("error", "Invalid menu selected");
            return false;
        }
        return true;
    }

    private boolean validateItemOwnership(Item item, Restaurant restaurant, RedirectAttributes redirectAttributes) {
        if (item == null || !itemService.belongsToRestaurant(item, restaurant)) {
            redirectAttributes.addFlashAttribute("error", "Unauthorized access to item or item not found");
            return false;
        }
        return true;
    }

    private void handleImageUpload(Item item, MultipartFile imageFile) {
        if (imageFile != null && !imageFile.isEmpty()) {
            String imageUrl = itemService.saveImage(imageFile);
            item.setImageUrl(imageUrl);
        }
    }

    private void createMenuItemRelationship(Menu menu, Item savedItem) {
        MenuItem menuItem = new MenuItem();
        menuItem.setMenu(menu);
        menuItem.setItem(savedItem);
        menuItemService.save(menuItem);
    }

    private void updateItemDetails(Item existingItem, Item newItemData, MultipartFile imageFile) {
        existingItem.setName(newItemData.getName());
        existingItem.setDescription(newItemData.getDescription());
        existingItem.setPrice(newItemData.getPrice());

        handleImageUpload(existingItem, imageFile);
    }
}