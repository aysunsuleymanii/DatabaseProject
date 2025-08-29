package mk.ukim.finki.easyfood.model;


import jakarta.persistence.*;

@Entity
@Table(name = "item_category")
@IdClass(ItemCategoryId.class)
public class ItemCategory {

    @Id
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Id
    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    // getters and setters
}
