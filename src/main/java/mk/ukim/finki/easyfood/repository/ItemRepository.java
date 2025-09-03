package mk.ukim.finki.easyfood.repository;

import mk.ukim.finki.easyfood.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long>, JpaSpecificationExecutor<Item> {
    Optional<Item> findByName(String name);

    List<Item> findAllByIdIn(List<Long> ids);
}
