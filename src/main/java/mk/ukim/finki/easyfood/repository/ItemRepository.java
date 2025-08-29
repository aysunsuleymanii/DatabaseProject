package mk.ukim.finki.easyfood.repository;

import mk.ukim.finki.easyfood.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {
}
