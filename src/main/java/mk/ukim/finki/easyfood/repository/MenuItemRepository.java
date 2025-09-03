package mk.ukim.finki.easyfood.repository;

import mk.ukim.finki.easyfood.model.Item;
import mk.ukim.finki.easyfood.model.Menu;
import mk.ukim.finki.easyfood.model.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    List<MenuItem> findByMenu(Menu menu);
    List<MenuItem> findByItem(Item item);
}
