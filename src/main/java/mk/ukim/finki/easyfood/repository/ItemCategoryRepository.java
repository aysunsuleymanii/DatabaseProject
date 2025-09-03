package mk.ukim.finki.easyfood.repository;

import mk.ukim.finki.easyfood.model.ItemCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public interface ItemCategoryRepository extends JpaRepository<ItemCategory, Long> {
}