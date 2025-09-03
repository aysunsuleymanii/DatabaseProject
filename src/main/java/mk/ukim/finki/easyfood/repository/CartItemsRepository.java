package mk.ukim.finki.easyfood.repository;

import mk.ukim.finki.easyfood.model.CartItems;
import mk.ukim.finki.easyfood.model.Customer;
import mk.ukim.finki.easyfood.model.Item;
import mk.ukim.finki.easyfood.model.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Repository
public interface CartItemsRepository extends JpaRepository<CartItems, Long> {
    Optional<CartItems> findByCartAndItem(ShoppingCart cart, Item item);

    Optional<CartItems> findByCart_CustomerAndItem(Customer customer, Item item);

    @Modifying
    @Transactional
    @Query("DELETE FROM CartItems c WHERE c.item.id = :itemId")
    void deleteByItemId(Long itemId);

    List<CartItems> findByCart_Customer_Id(Long id);
}
