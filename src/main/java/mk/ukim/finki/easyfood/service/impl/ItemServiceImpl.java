package mk.ukim.finki.easyfood.service.impl;

import mk.ukim.finki.easyfood.model.*;

import mk.ukim.finki.easyfood.repository.ItemRepository;
import mk.ukim.finki.easyfood.repository.MenuItemRepository;
import mk.ukim.finki.easyfood.repository.MenuRepository;
import mk.ukim.finki.easyfood.repository.RestaurantRepository;
import mk.ukim.finki.easyfood.service.ItemService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final MenuItemRepository menuItemRepository;
    private final MenuRepository menuRepository;
    private final RestaurantRepository restaurantRepository;

    public ItemServiceImpl(ItemRepository itemRepository,
                           MenuItemRepository menuItemRepository,
                           MenuRepository menuRepository,
                           RestaurantRepository restaurantRepository) {
        this.itemRepository = itemRepository;
        this.menuItemRepository = menuItemRepository;
        this.menuRepository = menuRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public List<Item> findAll() {
        return itemRepository.findAll();
    }

    @Override
    public List<Item> findRecommendedItems() {
        return itemRepository.findAll().stream().limit(5).collect(Collectors.toList());
    }

    @Override
    public List<Item> findItemsByRestaurantId(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        Menu menu = menuRepository.findByRestaurant(restaurant);
        if (menu == null) return List.of();

        List<MenuItem> menuItems = menuItemRepository.findByMenu(menu);
        return menuItems.stream()
                .map(MenuItem::getItem)
                .collect(Collectors.toList());
    }

}