package mk.ukim.finki.easyfood.model;

import java.io.Serializable;
import java.util.Objects;

public class CartItemsId implements Serializable {
    private Long cart;
    private Long item;

    public CartItemsId() {}

    public CartItemsId(Long cart, Long item) {
        this.cart = cart;
        this.item = item;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CartItemsId)) return false;
        CartItemsId that = (CartItemsId) o;
        return Objects.equals(cart, that.cart) && Objects.equals(item, that.item);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cart, item);
    }
}
