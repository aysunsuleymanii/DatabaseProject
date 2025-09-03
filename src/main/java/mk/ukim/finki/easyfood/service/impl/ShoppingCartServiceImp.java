package mk.ukim.finki.easyfood.service.impl;

import mk.ukim.finki.easyfood.model.Customer;
import mk.ukim.finki.easyfood.model.CartItems;
import mk.ukim.finki.easyfood.model.Item;
import mk.ukim.finki.easyfood.model.ShoppingCart;
import mk.ukim.finki.easyfood.repository.CartItemsRepository;
import mk.ukim.finki.easyfood.repository.CustomerRepository;
import mk.ukim.finki.easyfood.repository.ItemRepository;
import mk.ukim.finki.easyfood.repository.ShoppingCartRepository;
import mk.ukim.finki.easyfood.service.ShoppingCartService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ShoppingCartServiceImp implements ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;
    private final CustomerRepository customerRepository;
    private final ItemRepository itemRepository;
    private final CartItemsRepository cartItemsRepository;

    public ShoppingCartServiceImp(ShoppingCartRepository shoppingCartRepository,
                                  CustomerRepository customerRepository,
                                  ItemRepository itemRepository,
                                  CartItemsRepository cartItemsRepository) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.customerRepository = customerRepository;
        this.itemRepository = itemRepository;
        this.cartItemsRepository = cartItemsRepository;
    }

    @Override
    public List<CartItems> getItemsInCartByCustomerId(Long customerId) {
        Optional<Customer> customerOpt = customerRepository.findById(customerId);
        if (customerOpt.isEmpty()) {
            return Collections.emptyList();
        }

        Customer customer = customerOpt.get();
        Optional<ShoppingCart> cartOpt = shoppingCartRepository.findByCustomer(customer);

        if (cartOpt.isEmpty()) {
            return Collections.emptyList();
        }

        ShoppingCart cart = cartOpt.get();
        List<CartItems> items = cart.getCartItems();
        return items != null ? items : Collections.emptyList();
    }

    @Override
    public BigDecimal totalItemsPrice(Long customerId) {
        List<CartItems> cartItems = getItemsInCartByCustomerId(customerId);
        return cartItems.stream()
                .map(cartItem -> cartItem.getItem().getPrice()
                        .multiply(BigDecimal.valueOf(cartItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    @Transactional
    public void addItemToCart(Customer customer, Item item, int quantity) {
        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null.");
        }

        ShoppingCart cart = shoppingCartRepository.findByCustomer(customer).orElseGet(() -> {
            ShoppingCart newCart = new ShoppingCart();
            newCart.setCustomer(customer);
            return shoppingCartRepository.save(newCart);
        });

        if (cart.getCartItems() == null) {
            cart.setCartItems(new ArrayList<>());
        }

        Optional<CartItems> existingItem = cartItemsRepository.findByCartAndItem(cart, item);

        if (existingItem.isPresent()) {
            CartItems cartItem = existingItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            cartItemsRepository.save(cartItem);
        } else {
            CartItems newCartItem = new CartItems();
            newCartItem.setCart(cart);
            newCartItem.setItem(item);
            newCartItem.setQuantity(quantity);
            cartItemsRepository.save(newCartItem);
        }
    }

    @Override
    public List<CartItems> getCartForUser(Long userId) {
        return getItemsInCartByCustomerId(userId);
    }

    @Override
    @Transactional
    public void removeItemFromCart(Customer customer, Item item) {
        Optional<ShoppingCart> cartOptional = shoppingCartRepository.findByCustomer(customer);

        if (cartOptional.isPresent()) {
            ShoppingCart cart = cartOptional.get();
            Optional<CartItems> cartItemOptional = cartItemsRepository.findByCartAndItem(cart, item);

            if (cartItemOptional.isPresent()) {
                cartItemsRepository.delete(cartItemOptional.get());
            } else {
                throw new RuntimeException("Item not found in cart.");
            }
        } else {
            throw new RuntimeException("Cart not found for customer.");
        }
    }

    @Override
    @Transactional
    public void updateItemQuantity(Customer customer, Item item, int quantity) {
        if (quantity <= 0) {
            removeItemFromCart(customer, item);
        } else {
            Optional<ShoppingCart> cartOptional = shoppingCartRepository.findByCustomer(customer);

            if (cartOptional.isPresent()) {
                ShoppingCart cart = cartOptional.get();
                Optional<CartItems> cartItemOptional = cartItemsRepository.findByCartAndItem(cart, item);

                if (cartItemOptional.isPresent()) {
                    CartItems cartItem = cartItemOptional.get();
                    cartItem.setQuantity(quantity);
                    cartItemsRepository.save(cartItem);
                } else {
                    throw new RuntimeException("Item not found in cart.");
                }
            } else {
                throw new RuntimeException("Cart not found for customer.");
            }
        }
    }


    @Override
    public int getNumberOfItemsInCart(Long customerId) {
        return getItemsInCartByCustomerId(customerId).stream()
                .mapToInt(CartItems::getQuantity)
                .sum();
    }
}
