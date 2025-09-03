package mk.ukim.finki.easyfood.repository;

import mk.ukim.finki.easyfood.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long>, JpaSpecificationExecutor<Item> {
    Optional<Item> findByName(String name);

    List<Item> findAllByIdIn(List<Long> ids);

    @Query("SELECT i FROM Item i JOIN ItemCategory ic ON i.id = ic.item.id WHERE ic.category.id = :categoryId")
    List<Item> findItemsByCategoryId(@Param("categoryId") Long categoryId);

    @Query(value = """
                SELECT i.* FROM item i
                JOIN order_items oi ON i.item_id = oi.item_id
                GROUP BY i.item_id
                ORDER BY SUM(oi.quantity) DESC
                LIMIT 5
            """, nativeQuery = true)
    List<Item> findTop5PopularItems();


    @Query(value = """
                SELECT i.* FROM item i
                JOIN order_items oi ON i.item_id = oi.item_id
                JOIN orders o ON o.order_id = oi.order_id
                WHERE o.user_id = :userId
                GROUP BY i.item_id
                ORDER BY SUM(oi.quantity) DESC
                LIMIT 5
            """, nativeQuery = true)
    List<Item> findTop5ItemsForUser(@Param("userId") Long userId);


    @Query(value = """
                SELECT i.* 
                FROM item i
                JOIN order_items oi ON i.item_id = oi.item_id
                JOIN order_items oi2 ON oi2.order_id = oi.order_id
                WHERE oi2.item_id IN :cartItemIds AND i.item_id NOT IN :cartItemIds
                GROUP BY i.item_id
                ORDER BY COUNT(*) DESC
                LIMIT 5
            """, nativeQuery = true)
    List<Item> findItemsBoughtTogether(@Param("cartItemIds") List<Long> cartItemIds);

}

