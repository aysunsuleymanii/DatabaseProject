package mk.ukim.finki.easyfood.repository;

import mk.ukim.finki.easyfood.model.Item;
import mk.ukim.finki.easyfood.model.Menu;
import mk.ukim.finki.easyfood.model.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    List<MenuItem> findByMenu(Menu menu);

    List<MenuItem> findByItem(Item item);

    Optional<MenuItem> findFirstByItem(Item item);

    List<MenuItem> findByMenuId(Long menuId);

    void deleteByItemId(Long itemId);


    MenuItem findByMenuAndItem(Menu menu, Item item);

    void deleteByItem(Item item);

    void deleteByMenu(Menu menu);
}
