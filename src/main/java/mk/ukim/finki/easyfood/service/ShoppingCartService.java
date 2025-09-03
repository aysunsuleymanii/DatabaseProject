package mk.ukim.finki.easyfood.service;

import mk.ukim.finki.easyfood.model.CartItems;
import mk.ukim.finki.easyfood.model.Customer;
import mk.ukim.finki.easyfood.model.Item;
import mk.ukim.finki.easyfood.model.ShoppingCart;

import java.math.BigDecimal;
import java.util.List;

public interface ShoppingCartService {

    public List<CartItems> getItemsInCartByCustomerId(Long customerId);

    public BigDecimal totalItemsPrice(Long customerId);

    void addItemToCart(Customer customer, Item item, int quantity);

    List<CartItems> getCartForUser(Long userId);

    void removeItemFromCart(Customer customer, Item item);

    void updateItemQuantity(Customer customer, Item item, int quantity);

    int getNumberOfItemsInCart(Long customerId);

    public ShoppingCart getActiveShoppingCart(Long userId);

    public void deleteShoppingCart(Long userId);

    List<CartItems> getCartItems(Long customerId);

    void clearCart(Long customerId);
}

