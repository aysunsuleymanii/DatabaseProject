package mk.ukim.finki.easyfood.service.impl;

import mk.ukim.finki.easyfood.model.CartItems;
import mk.ukim.finki.easyfood.model.ShoppingCart;
import mk.ukim.finki.easyfood.repository.CartItemsRepository;
import mk.ukim.finki.easyfood.repository.ShoppingCartRepository;
import mk.ukim.finki.easyfood.service.CartItemsService;
import org.springframework.stereotype.Service;

@Service
public class CartItemsServiceImpl implements CartItemsService {
    private final CartItemsRepository cartItemsRepository;
    private final ShoppingCartRepository shoppingCartRepository;

    public CartItemsServiceImpl(CartItemsRepository cartItemsRepository, ShoppingCartRepository shoppingCartRepository) {
        this.cartItemsRepository = cartItemsRepository;
        this.shoppingCartRepository = shoppingCartRepository;
    }



}
