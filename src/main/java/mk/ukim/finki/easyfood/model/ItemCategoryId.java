package mk.ukim.finki.easyfood.model;


import java.io.Serializable;
import java.util.Objects;

public class ItemCategoryId implements Serializable {
    private Long category;
    private Long item;

    public ItemCategoryId() {}

    public ItemCategoryId(Long category, Long item) {
        this.category = category;
        this.item = item;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ItemCategoryId)) return false;
        ItemCategoryId that = (ItemCategoryId) o;
        return Objects.equals(category, that.category) && Objects.equals(item, that.item);
    }

    @Override
    public int hashCode() {
        return Objects.hash(category, item);
    }
}
