package mk.ukim.finki.easyfood.model;


import jakarta.persistence.*;

@Entity
@Table(name = "menu_item")
@IdClass(MenuItemId.class)
public class MenuItem {

    @Id
    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @Id
    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    // getters and setters
}

