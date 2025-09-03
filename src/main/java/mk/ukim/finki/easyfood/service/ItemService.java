package mk.ukim.finki.easyfood.service;

import mk.ukim.finki.easyfood.model.Item;
import org.springframework.data.domain.Page;

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
}
