package mk.ukim.finki.easyfood.service.impl;

import jakarta.persistence.criteria.Join;
import mk.ukim.finki.easyfood.model.*;

import mk.ukim.finki.easyfood.model.exceptions.ItemNotFoundException;
import mk.ukim.finki.easyfood.repository.ItemRepository;
import mk.ukim.finki.easyfood.repository.MenuItemRepository;
import mk.ukim.finki.easyfood.repository.MenuRepository;
import mk.ukim.finki.easyfood.repository.RestaurantRepository;
import mk.ukim.finki.easyfood.service.ItemService;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import jakarta.persistence.criteria.Predicate; // This is the correct one

import java.util.ArrayList;
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

//    @Override
//    public List<Item> findItemsByRestaurantId(Long restaurantId) {
//        Restaurant restaurant = restaurantRepository.findById(restaurantId)
//                .orElseThrow(() -> new RuntimeException("Restaurant not found"));
//
//        Menu menu = menuRepository.findByRestaurant(restaurant);
//        if (menu == null) return List.of();
//
//        List<MenuItem> menuItems = menuItemRepository.findByMenu(menu);
//        return menuItems.stream()
//                .map(MenuItem::getItem)
//                .collect(Collectors.toList());
//    }

    public List<Item> getItemsByMenuId(Long menuId) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new RuntimeException("Menu not found"));

        List<MenuItem> menuItems = menuItemRepository.findByMenu(menu);

        return menuItems.stream()
                .map(MenuItem::getItem)
                .collect(Collectors.toList());
    }

    @Override
    public Item findById(Long itemId) {
        return this.itemRepository.findById(itemId).orElseThrow(() -> new ItemNotFoundException(itemId));
    }

    @Override
    public List<Item> searchItems(String searchTerm) {
        Specification<Item> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (searchTerm != null && !searchTerm.trim().isEmpty()) {
                String likePattern = "%" + searchTerm.toLowerCase() + "%";
                Predicate itemNamePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), likePattern);

                Join<Item, Object> menuItemJoin = root.join("menuItems");

                Join<Object, Object> menuJoin = menuItemJoin.join("menu");

                Join<Object, Object> restaurantJoin = menuJoin.join("restaurant");

                Predicate restaurantNamePredicate = criteriaBuilder.like(criteriaBuilder.lower(restaurantJoin.get("name")), likePattern);

                query.distinct(true);

                predicates.add(criteriaBuilder.or(itemNamePredicate, restaurantNamePredicate));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        return itemRepository.findAll(specification);
    }

}