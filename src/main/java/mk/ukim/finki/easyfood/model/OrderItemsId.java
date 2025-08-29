package mk.ukim.finki.easyfood.model;


import java.io.Serializable;
import java.util.Objects;

public class OrderItemsId implements Serializable {
    private Long order;
    private Long item;

    public OrderItemsId() {}

    public OrderItemsId(Long order, Long item) {
        this.order = order;
        this.item = item;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderItemsId)) return false;
        OrderItemsId that = (OrderItemsId) o;
        return Objects.equals(order, that.order) && Objects.equals(item, that.item);
    }

    @Override
    public int hashCode() {
        return Objects.hash(order, item);
    }
}

