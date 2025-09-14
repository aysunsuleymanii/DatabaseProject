package mk.ukim.finki.easyfood.repository;

import mk.ukim.finki.easyfood.model.Menu;
import mk.ukim.finki.easyfood.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {


    Menu findByRestaurant(Restaurant restaurant);

    @Query("SELECT m FROM Menu m WHERE m.restaurant.id = :restaurantId")
    Menu findByRestaurantId(@Param("restaurantId") Long restaurantId);

    boolean existsByRestaurant(Restaurant restaurant);


    Menu findByRestaurantAndName(Restaurant restaurant, String name);

    @Query("SELECT DISTINCT m.restaurant FROM Menu m")
    List<Restaurant> findAllRestaurantsWithMenus();


    @Query("SELECT SIZE(m.menuItems) FROM Menu m WHERE m.id = :menuId")
    int countItemsInMenu(@Param("menuId") Long menuId);


    @Query("SELECT m FROM Menu m ORDER BY m.restaurant.name ASC")
    List<Menu> findAllOrderByRestaurantName();


    @Query("SELECT m FROM Menu m WHERE LOWER(m.restaurant.name) LIKE LOWER(CONCAT('%', :restaurantName, '%'))")
    List<Menu> findByRestaurantNameContainingIgnoreCase(@Param("restaurantName") String restaurantName);


    @Query("SELECT m FROM Menu m WHERE SIZE(m.menuItems) > 0")
    List<Menu> findMenusWithItems();


    @Query("SELECT m FROM Menu m WHERE SIZE(m.menuItems) = 0")
    List<Menu> findMenusWithoutItems();


    @Query("SELECT m FROM Menu m WHERE LOWER(m.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Menu> findByNameContainingIgnoreCase(@Param("name") String name);
}