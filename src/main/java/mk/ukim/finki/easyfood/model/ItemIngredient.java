package mk.ukim.finki.easyfood.model;


import jakarta.persistence.*;

@Entity
@Table(name = "item_ingredient")
@IdClass(ItemIngredientId.class)
public class ItemIngredient {

    @Id
    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    @Id
    @ManyToOne
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredient;

    @Column(name = "quantity", length = 50)
    private String quantity;

    // getters and setters
}
