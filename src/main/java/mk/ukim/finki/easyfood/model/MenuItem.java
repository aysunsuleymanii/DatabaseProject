package mk.ukim.finki.easyfood.model;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@Entity
@Table(name = "menu_item")
@IdClass(MenuItemId.class)
public class MenuItem implements Serializable {

    @Id
    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @Id
    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    public Item getItem() {
        return item;
    }


    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public void setItem(Item item) {
        this.item = item;
    }


}
