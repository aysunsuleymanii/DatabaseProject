package mk.ukim.finki.easyfood.repository;

import mk.ukim.finki.easyfood.model.ItemIngredient;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ItemIngredientRepository extends JpaSpecificationRepository<ItemIngredient, Long> {
}
