package mk.ukim.finki.easyfood.service;

import mk.ukim.finki.easyfood.model.CartItems;

import java.math.BigDecimal;
import java.util.List;

public interface ShoppingCartService {

    public List<CartItems> getItemsInCartByCustomerId(Long customerId);

    public BigDecimal totalItemsPrice(Long customerId);
}
