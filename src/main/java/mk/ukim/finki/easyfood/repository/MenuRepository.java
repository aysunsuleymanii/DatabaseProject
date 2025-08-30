package mk.ukim.finki.easyfood.repository;

import mk.ukim.finki.easyfood.model.Menu;
import mk.ukim.finki.easyfood.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {
    Menu findByRestaurantId(Long restaurantId);
}
