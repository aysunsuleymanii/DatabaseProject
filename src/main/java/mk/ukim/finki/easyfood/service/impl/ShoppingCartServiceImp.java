package mk.ukim.finki.easyfood.service.impl;

import mk.ukim.finki.easyfood.model.Customer;
import mk.ukim.finki.easyfood.model.CartItems;
import mk.ukim.finki.easyfood.model.ShoppingCart;
import mk.ukim.finki.easyfood.repository.CustomerRepository;
import mk.ukim.finki.easyfood.repository.ShoppingCartRepository;
import mk.ukim.finki.easyfood.service.ShoppingCartService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@Service
public class ShoppingCartServiceImp implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final CustomerRepository customerRepository;

    public ShoppingCartServiceImp(ShoppingCartRepository shoppingCartRepository, CustomerRepository customerRepository) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.customerRepository = customerRepository;
    }


    @Override
    public List<CartItems> getItemsInCartByCustomerId(Long customerId) {
        Customer customer = customerRepository.findById(customerId).orElse(null);


        // Find the shopping cart associated with the customer.
        return shoppingCartRepository.findByCustomer(customer)
                .map(ShoppingCart::getCartItems)
                .orElse(Collections.emptyList());
    }

    @Override
    public BigDecimal totalItemsPrice(Long customerId) {
        // Retrieve the list of CartItems for the given customer.
        List<CartItems> cartItems = getItemsInCartByCustomerId(customerId);

        // Use a stream to calculate the total price.
        return cartItems.stream()
                // Map each CartItems object to its calculated subtotal.
                .map(cartItem -> cartItem.getItem().getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())))
                // Reduce the stream to a single BigDecimal value by summing up all subtotals.
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }


}
