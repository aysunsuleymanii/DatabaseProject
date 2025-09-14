package mk.ukim.finki.easyfood.service.impl;

import jakarta.persistence.criteria.Join;
import mk.ukim.finki.easyfood.model.*;

import mk.ukim.finki.easyfood.model.exceptions.ItemNotFoundException;
import mk.ukim.finki.easyfood.repository.*;
import mk.ukim.finki.easyfood.service.ItemService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import jakarta.persistence.criteria.Predicate; // This is the correct one
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;


    private final ItemRepository itemRepository;
    private final MenuItemRepository menuItemRepository;
    private final MenuRepository menuRepository;
    private final RestaurantRepository restaurantRepository;
    private final CartItemsRepository cartItemsRepository;

    public ItemServiceImpl(ItemRepository itemRepository,
                           MenuItemRepository menuItemRepository,
                           MenuRepository menuRepository,
                           RestaurantRepository restaurantRepository, CartItemsRepository cartItemsRepository) {
        this.itemRepository = itemRepository;
        this.menuItemRepository = menuItemRepository;
        this.menuRepository = menuRepository;
        this.restaurantRepository = restaurantRepository;
        this.cartItemsRepository = cartItemsRepository;
    }

    @Override
    public List<Item> findAll() {
        return itemRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Item> findRecommendedItems(Long userId) {
        List<CartItems> cartItems = cartItemsRepository.findByCart_Customer_Id(userId);
        List<Long> cartItemIds = cartItems.stream()
                .map(ci -> ci.getItem().getId())
                .toList();

        if (cartItemIds.isEmpty()) {
            return itemRepository.findTop5PopularItems();
        } else {
            return itemRepository.findItemsBoughtTogether(cartItemIds);
        }
    }


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

    @Override
    public List<Item> findItemsByCategoryId(Long categoryId) {
        return this.itemRepository.findItemsByCategoryId(categoryId);
    }

    @Override
    public Item save(Item item) {
        return itemRepository.save(item);
    }

    @Override
    public void delete(Long id) {
        itemRepository.deleteById(id);
    }

    @Override
    public List<Item> findByRestaurantMenus(Restaurant restaurant) {
        return itemRepository.findByRestaurantMenus(restaurant.getId());
    }

    @Override
    public boolean belongsToRestaurant(Item item, Restaurant restaurant) {
        List<Item> restaurantItems = findByRestaurantMenus(restaurant);
        return restaurantItems.stream().anyMatch(i -> i.getId().equals(item.getId()));
    }

    @Override
    public String saveImage(MultipartFile imageFile) {
        try {
            if (imageFile.isEmpty()) {
                return null;
            }

            Path uploadPath = Paths.get("src/main/resources/static/images/media");
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String originalFilename = imageFile.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String filename = UUID.randomUUID().toString() + extension;

            Path filePath = uploadPath.resolve(filename);
            Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return "/images/media/" + filename;

        } catch (IOException e) {
            throw new RuntimeException("Failed to store image file", e);
        }
    }

    @Override
    public List<Item> searchByName(String name, Restaurant restaurant) {
        List<Item> allRestaurantItems = findByRestaurantMenus(restaurant);
        return allRestaurantItems.stream()
                .filter(item -> item.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }
}