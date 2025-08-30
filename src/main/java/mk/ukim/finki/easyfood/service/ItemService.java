package mk.ukim.finki.easyfood.service;

import mk.ukim.finki.easyfood.model.Item;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ItemService {
    List<Item> findAll();
    List<Item> findRecommendedItems();
    List<Item> findItemsByRestaurantId(Long restaurantId);
}
