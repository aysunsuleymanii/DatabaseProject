package mk.ukim.finki.easyfood.model;

import jakarta.persistence.*;

@Entity
@Table(name = "delivery_man")
@PrimaryKeyJoinColumn(name = "user_id")
public class DeliveryMan extends AppUser {

}

