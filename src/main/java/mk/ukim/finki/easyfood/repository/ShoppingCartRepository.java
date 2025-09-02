package mk.ukim.finki.easyfood.repository;

import mk.ukim.finki.easyfood.model.Customer;
import mk.ukim.finki.easyfood.model.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {

    Optional<ShoppingCart> findByCustomer(Customer customer);

}
