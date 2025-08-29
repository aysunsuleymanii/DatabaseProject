package mk.ukim.finki.easyfood.model;

import java.io.Serializable;
import java.util.Objects;

public class ItemIngredientId implements Serializable {
    private Long item;
    private Long ingredient;

    public ItemIngredientId() {}

    public ItemIngredientId(Long item, Long ingredient) {
        this.item = item;
        this.ingredient = ingredient;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ItemIngredientId)) return false;
        ItemIngredientId that = (ItemIngredientId) o;
        return Objects.equals(item, that.item) && Objects.equals(ingredient, that.ingredient);
    }

    @Override
    public int hashCode() {
        return Objects.hash(item, ingredient);
    }
}
