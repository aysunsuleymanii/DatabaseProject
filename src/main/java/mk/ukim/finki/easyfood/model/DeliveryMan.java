package mk.ukim.finki.easyfood.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import mk.ukim.finki.easyfood.model.enumerations.ROLE;

@Entity
@Table(name = "delivery_man")
@PrimaryKeyJoinColumn(name = "user_id")
@Data
@NoArgsConstructor
public class DeliveryMan extends AppUser {
    public DeliveryMan(String email, String password, String firstName, String lastName, String phone, ROLE role) {
        super(email, password, firstName, lastName, phone, role);
    }
}

