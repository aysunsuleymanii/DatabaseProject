package mk.ukim.finki.easyfood.model;

import jakarta.persistence.*;

@Entity
@Table(name = "cart_items")
@IdClass(CartItemsId.class)
public class CartItems {

    @Id
    @ManyToOne
    @JoinColumn(name = "cart_id")
    private ShoppingCart cart;

    @Id
    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    // getters and setters

    public Item getItem() {
        return item;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setCart(ShoppingCart cart) {
        this.cart = cart;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
