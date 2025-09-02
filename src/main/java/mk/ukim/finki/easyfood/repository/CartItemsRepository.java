package mk.ukim.finki.easyfood.repository;

import mk.ukim.finki.easyfood.model.CartItems;
import mk.ukim.finki.easyfood.model.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CartItemsRepository extends JpaRepository<CartItems, Long> {
}
