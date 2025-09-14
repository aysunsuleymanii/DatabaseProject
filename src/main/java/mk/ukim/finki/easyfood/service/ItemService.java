package mk.ukim.finki.easyfood.service;

import mk.ukim.finki.easyfood.model.Item;
import mk.ukim.finki.easyfood.model.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ItemService {
    List<Item> findAll();

    List<Item> findRecommendedItems(Long userId);

    public List<Item> getItemsByMenuId(Long menuId);

    Item findById(Long itemId);

    List<Item> searchItems(String searchTerm);

    List<Item> findItemsByCategoryId(Long categoryId);

    Item save(Item item);

    void delete(Long id);

    List<Item> findByRestaurantMenus(Restaurant restaurant);

    boolean belongsToRestaurant(Item item, Restaurant restaurant);

    String saveImage(MultipartFile imageFile);

    List<Item> searchByName(String name, Restaurant restaurant);
}
